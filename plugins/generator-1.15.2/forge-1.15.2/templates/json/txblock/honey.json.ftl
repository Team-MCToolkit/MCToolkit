<#include "../textures.ftl">
{
    "parent": "block/honey_block",
    "textures": {
        <#if data.particleTexture?has_content>
        "particle": "${mappedSingleTexture(data.particleTexture, "blocks", modid)}",
        <#else>
        "particle": "${mappedSingleTexture(data.texture, "blocks", modid)}",
        </#if>
        "down": "${mappedSingleTexture(data.texture, "blocks", modid)}",
        "up": "${mappedSingleTexture(data.textureTop, "blocks", modid)}",
        "side": "${mappedSingleTexture(data.textureFront, "blocks", modid)}"
    }
}
