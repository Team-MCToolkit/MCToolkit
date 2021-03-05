<#-- @formatter:off -->
<#include "../textures.ftl">
{
    "textures": {
        <#if data.particleTexture?has_content>
        "particle": "${mappedSingleTexture(data.particleTexture, "blocks", modid)}",
        <#else>
        <#if data.textureFront?has_content>
        "particle": "${mappedSingleTexture(data.textureFront, "blocks", modid)}",
        <#else>
        "particle": "${mappedSingleTexture(data.texture, "blocks", modid)}",
        </#if>
        </#if>
        "bottom": "${mappedSingleTexture(data.texture, "blocks", modid)}",
        <#if data.textureTop?has_content>
        "top": "${mappedSingleTexture(data.textureTop, "blocks", modid)}",
        <#else>
        "top": "${mappedSingleTexture(data.texture, "blocks", modid)}",
        </#if>
        <#if data.textureFront?has_content>
        "side": "${mappedSingleTexture(data.textureFront, "blocks", modid)}"
        <#else>
        "side": "${mappedSingleTexture(data.texture, "blocks", modid)}"
        </#if>
    },
    "elements": [
        {   "from": [ 1, 0, 1 ],
            "to": [ 15, 8, 15 ],
            "faces": {
                "down":  { "texture": "#bottom", "cullface": "down" },
                "up":    { "texture": "#top" },
                "north": { "texture": "#side" },
                "south": { "texture": "#side" },
                "west":  { "texture": "#side" },
                "east":  { "texture": "#side" }
            }
        }
    ]
}

<#-- @formatter:on -->