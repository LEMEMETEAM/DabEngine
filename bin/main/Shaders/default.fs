#version 330 core
#extension GL_ARB_shading_language_420pack: enable

in vec3 outPosition;
in vec4 outColor;
in vec2 outTexCord;
in vec3 outNormal;

uniform vec3 viewPos = vec3(0.0);
#if defined(LAMBERT) || defined(BLINN)
    #include /Shaders/lighting.h

    layout (std140) uniform lighting{
        Light[32] lights;
        float ambientStrength;
    };

    Surface surf;

    #ifdef SPEC_MAP
        layout(binding=1) uniform sampler2D specMap;
    #endif
    #ifdef NORMAL_MAP
        layout(binding=2) uniform sampler2D normalmap;
    #endif
#endif

#ifdef TEXT
    #include /Shaders/text.h
#endif

#include /Shaders/textured.h
//imports utils as well

layout(binding=0) uniform sampler2D texture;

out vec4 fragColor;

void main(){
    vec4 finalColor = outColor;

    #ifdef UNSHADED
        #ifdef TEXTURED
            finalColor *= getTexel(texture, outTexCord);
        #endif
    #endif

    #ifdef HDR
        vec3 hdrColor = getTexel(texture, outTexCord).rgb;
        finalColor *= vec4(hdrColor / (hdrColor + vec3(1.0)), 1.0);
    #endif

    #if defined(LAMBERT) || defined(BLINN)
        #ifdef TEXTURED
            surf.albedo = getTexel(texture, outTexCord).rgb;
        #else
            surf.albedo = outColor.rgb;
        #endif
        #ifdef NORMAL_MAP
            surf.normal = (getTexel(normalmap, outTexCord).rgb * 2) - 1;
        #else
            surf.normal = outNormal;
        #endif
        #ifdef SPEC_MAP
            surf.gloss = getTexel(specMap, outTexCord).rgb;
        #else
            surf.gloss = vec3(0.5);
        #endif
        //TODO ADD POINT LIGHTS
        vec3 c = vec3(0.0);
        for(int i = 0; i < lights.length(); i++){
            #ifdef LAMBERT
                c += lambertLighting(surf, lights[i], normalize(lights[i].position - outPosition), length(lights[i].position - outPosition));
            #elif defined(BLINN)
                c += blinnPhongLighting(surf, lights[i], normalize(lights[i].position - outPosition), normalize(viewPos - outPosition), length(lights[i].position - outPosition));
            #endif
        }

        finalColor *= vec4(c, 1.0);
    #endif

    #ifdef TEXT
        vec4 sampled = sampleText(texture, outTexCord);
        finalColor *= sampled;
    #endif

    #ifdef FOG
        finalColor = calcFog(vec4(0.6, 0.8, 1.0, 1.0), finalColor, FOG);
    #endif

    #ifdef SRGB
        float gamma = 2.2;
        fragColor = pow(finalColor, vec4(1.0/gamma));
    #else   
        fragColor = finalColor;
    #endif
}
