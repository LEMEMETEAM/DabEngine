#version 330 core
#extension GL_ARB_shading_language_420pack: enable

in vec3 outPosition;
in vec4 outColor;

#include /Shaders/lighting.h
#include /Shaders/text.h

layout(binding=1) uniform sampler2D specMap;
layout(binding=2) uniform sampler2D normalmap;
uniform vec3 viewPos = vec3(0.0);

out vec4 fragColor;

void main(){
    vec4 finalColor = outColor;

    #if defined(TEXTURED) && !defined(LIT)
        finalColor *= getTexel();
    #endif

    #ifdef LIT
        vec4 color;
        for(int i = 0; i < lights.length(); i++){
            vec4 ambient = calcAmbient(i) * getTexel();
            #ifdef NORMAL_MAP
                vec4 tex = texture(normalmap, outTexCord);
                tex = (tex * 2) - 1;
                vec4 diffuse = calcDiffuse(i, tex.xyz, outPosition) * getTexel();
                #ifdef SPEC_MAP
                    vec4 specular = calcSpecular(i, viewPos, tex.xyz, outPosition, 0.5, 32) * texture(specMap, outTexCord);
                #else
                    vec4 specular = calcSpecular(i, viewPos, tex.xyz, outPosition, 0.5, 32);
                #endif
            #else
                vec4 diffuse = calcDiffuse(i, outNormal, outPosition) * getTexel();
                #ifdef SPEC_MAP
                    vec4 specular = calcSpecular(i, viewPos, outNormal, outPosition, 0.5, 32) * texture(specMap, outTexCord);
                #else
                    vec4 specular = calcSpecular(i, viewPos, outNormal, outPosition, 0.5, 32);
                #endif
            #endif
            color += (specular + diffuse + ambient);
        }
        finalColor *= vec4(color.rgb, 1);
    #endif

    #ifdef TEXT
        vec4 sampled = sampleText();
        finalColor *= sampled;
    #endif

    #ifdef FOG
        float z = gl_FragCoord.z / gl_FragCoord.w;
        float fog = clamp(exp(-FOG * z * z), 0.2, 1);
        finalColor = mix(vec4(0.6, 0.8, 1.0, 1.0), finalColor, fog);
    #endif

    fragColor = finalColor;
}
