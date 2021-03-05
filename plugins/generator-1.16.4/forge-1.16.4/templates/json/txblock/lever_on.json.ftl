<#include "../textures.ftl">
{
    "parent" : "block/lever_on",
    "textures": {
        <#if data.particleTexture?has_content>
        "particle": "${mappedSingleTexture(data.particleTexture, "blocks", modid)}",
        </#if>
        "base": "${mappedSingleTexture(data.texture, "blocks", modid)}",
        <#if data.textureTop?has_content>
        "lever": "${mappedSingleTexture(data.textureTop, "blocks", modid)}"
        <#else>
        "lever": "${mappedSingleTexture(data.texture, "blocks", modid)}"
        </#if>
    }
}
