<#include "../textures.ftl">
{
    "parent": "block/slime_block",
    "textures": {
        <#if data.particleTexture?has_content>
        "particle": "${mappedSingleTexture(data.particleTexture, "blocks", modid)}",
        <#else>
        "particle": "${mappedSingleTexture(data.texture, "blocks", modid)}",
        </#if>
        "texture": "${mappedSingleTexture(data.texture, "blocks", modid)}"
    }
}
