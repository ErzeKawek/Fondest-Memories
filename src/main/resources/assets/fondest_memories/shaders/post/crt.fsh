#version 330
#extension GL_ARB_separate_shader_objects : require

#include <minecraft:globals.glsl>

uniform sampler2D InSampler;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout(std140) uniform VhsConfig {
    float Intensity;
    float TimeScale;

    float ScanlineIntensity;
    float ScanlineCount;
    float ScreenWobble;
    float TrackingError;
    float VignetteAmount;
    float VignetteRoundness;

    float RedOffsetX;
    float RedOffsetY;
    float GreenOffsetX;
    float GreenOffsetY;
    float BlueOffsetX;
    float BlueOffsetY;
    float ChromaBleedRight;

    float LumaNoise;
    float ChromaNoise;
    float TapeGrain;

    float Saturation;
    float Contrast;
    float Brightness;
    float Gamma;
    float TintR;
    float TintG;
    float TintB;

    float BlurRadius;
    float EdgeSharpen;
    float HorizontalSmear;

    float DropoutAmount;
    float DropoutLength;
};

layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;

const float PI = 3.14159265359;

float hash11(float p) {
    p = fract(p * 0.1031);
    p *= p + 33.33;
    p *= p + p;
    return fract(p);
}

float hash21(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

vec3 toYIQ(vec3 c) {
    return vec3(
        dot(c, vec3(0.299, 0.587, 0.114)),
        dot(c, vec3(0.596, -0.274, -0.322)),
        dot(c, vec3(0.211, -0.523, 0.312))
    );
}

vec3 fromYIQ(vec3 y) {
    return vec3(
        y.x + 0.956 * y.y + 0.621 * y.z,
        y.x - 0.272 * y.y - 0.647 * y.z,
        y.x - 1.106 * y.y + 1.703 * y.z
    );
}

vec3 tex(vec2 uv) {
    return texture(InSampler, clamp(uv, vec2(0.0), vec2(1.0))).rgb;
}

void main() {
    vec2 res = InSize;
    vec2 texel = 1.0 / res;
    float t = GameTime * 1200.0 * TimeScale;

    vec3 original = tex(texCoord);
    vec2 uv = texCoord;

    // wobble
    uv.x += ScreenWobble * sin(uv.y * 18.0 + t * 2.5)
          + ScreenWobble * 0.5 * sin(uv.y * 63.0 - t * 7.0);

    // tracking error
    float trackY = fract(1.0 - t * 0.15);
    float dTrack = abs(uv.y - trackY);
    dTrack = min(dTrack, 1.0 - dTrack);
    float band = exp(-dTrack * dTrack * 900.0);
    float row = floor(uv.y * 240.0) + floor(t * 30.0) * 17.0;
    uv.x += TrackingError * band * (hash11(row) - 0.5) * 6.0;
    uv.x += TrackingError * band * 2.0;

    // Chromatic aberration
    vec3 col;
    col.r = tex(uv + vec2(RedOffsetX, RedOffsetY)).r;
    col.g = tex(uv + vec2(GreenOffsetX, GreenOffsetY)).g;
    col.b = tex(uv + vec2(BlueOffsetX, BlueOffsetY)).b;

    // Chroma bleed
    vec3 yiq = toYIQ(col);
    vec2 iq = vec2(0.0);
    const int BLEED_STEPS = 6;
    for (int i = 0; i < BLEED_STEPS; i++) {
        float k = float(i) / float(BLEED_STEPS - 1);
        iq += toYIQ(tex(uv - vec2(ChromaBleedRight * k, 0.0))).yz;
    }
    iq /= float(BLEED_STEPS);
    yiq.yz = iq;
    col = fromYIQ(yiq);

    // luma smear
    vec3 smear = vec3(0.0);
    const int SMEAR_STEPS = 4;
    for (int i = 1; i <= SMEAR_STEPS; i++) {
        float k = float(i) / float(SMEAR_STEPS);
        smear += tex(uv - vec2(HorizontalSmear * k, 0.0)) * (1.0 - k * 0.6);
    }
    smear /= 2.8;
    col = mix(col, max(col, smear), clamp(HorizontalSmear * 40.0, 0.0, 1.0) * 0.5);

    // blur + sharpen
    vec3 blurred = vec3(0.0);
    const int RING = 8;
    for (int i = 0; i < RING; i++) {
        float a = 2.0 * PI * float(i) / float(RING);
        blurred += tex(uv + vec2(cos(a), sin(a)) * BlurRadius * texel);
    }
    blurred = (blurred + col) / float(RING + 1);
    vec3 soft = mix(col, blurred, clamp(BlurRadius / 3.0, 0.0, 1.0));
    col = soft + EdgeSharpen * (col - blurred);

    // Scanlines
    float scan = 0.5 + 0.5 * sin(uv.y * ScanlineCount * 2.0 * PI);
    col *= 1.0 - ScanlineIntensity * scan;

    // Noise and grain
    vec2 px = floor(uv * res);
    float frame = floor(t * 30.0);

    float n = hash21(px + frame * 13.37) - 0.5;
    col += n * LumaNoise;

    vec3 cn = vec3(
        hash21(px + frame * 7.1 + 1.0),
        hash21(px + frame * 3.7 + 2.0),
        hash21(px + frame * 5.3 + 3.0)
    ) - 0.5;
    col += cn * ChromaNoise * 0.25;

    // tape grain
    float grain = hash21(vec2(floor(uv.x * 80.0), floor(uv.y * 480.0) + frame * 11.0)) - 0.5;
    col += grain * TapeGrain;

    // dropout streaks
    // fading to the right
    float dropFrame = floor(t * 12.0);
    float rowId = floor(uv.y * res.y / 2.0);
    float segPx = DropoutLength * 3.0;
    float xPix = uv.x * res.x;
    float drop = 0.0;
    for (int k = 0; k < 2; k++) {
        float cell = floor(xPix / segPx) - float(k);
        float h = hash21(vec2(cell, rowId) + dropFrame * 37.0);
        if (h > 1.0 - DropoutAmount) {
            float start = (cell + hash21(vec2(rowId, cell) + dropFrame * 11.0)) * segPx;
            float d = xPix - start;
            if (d >= 0.0 && d < DropoutLength) {
                drop = max(drop, pow(1.0 - d / DropoutLength, 2.0));
            }
        }
    }
    col = mix(col, vec3(0.93, 0.97, 1.0), drop);

    // Vignette
    vec2 q = abs(uv - 0.5) * 2.0;
    float d = mix(max(q.x, q.y), length(q), clamp(VignetteRoundness, 0.0, 1.0));
    col *= 1.0 - VignetteAmount * smoothstep(0.4, 1.25, d);

    // Color grading
    float luma = dot(col, vec3(0.299, 0.587, 0.114));
    col = mix(vec3(luma), col, Saturation);
    col = (col - 0.5) * Contrast + 0.5;
    col *= Brightness;
    col = pow(max(col, vec3(0.0)), vec3(Gamma));
    col *= vec3(TintR, TintG, TintB);

    fragColor = vec4(mix(original, clamp(col, 0.0, 1.0), Intensity), 1.0);
}
