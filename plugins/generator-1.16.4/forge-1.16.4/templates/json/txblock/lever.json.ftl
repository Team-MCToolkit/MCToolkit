<#include "../textures.ftl">
{
    "parent" : "block/lever",
    "textures": {
        <#if data.particleTexture?has_content>
        "particle": "${mappedSingleTexture(data.particleTexture, "blocks", modid)}",
        <#else>
        <#if data.textureTop?has_content>
        "particle": "${mappedSingleTexture(data.textureTop, "blocks", modid)}",
        <#else>
        "particle": "${mappedSingleTexture(data.texture, "blocks", modid)}",
        </#if>
        </#if>
        "base": "${mappedSingleTexture(data.texture, "blocks", modid)}",
        <#if data.textureTop?has_content>
        "lever": "${mappedSingleTexture(data.textureTop, "blocks", modid)}"
        <#else>
        "lever": "${mappedSingleTexture(data.texture, "blocks", modid)}"
        </#if>
    }
}
