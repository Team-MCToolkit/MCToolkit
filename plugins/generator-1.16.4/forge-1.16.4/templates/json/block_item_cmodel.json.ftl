<#-- @formatter:off -->
{
    "parent": "item/handheld",
    "textures": {
        <#if data.itemTexture?has_content>
            <#if data.itemTexture.endsWith(".png")>
            "layer0": "${data.itemTexture.replace(".png", "")}"
            <#else>
            "layer0": "${modid}:items/${data.itemTexture}"
            </#if>
        <#else>
            <#if data.texture.endsWith(".png")>
                "layer0": "${data.texture.replace(".png", "")}"
            <#else>
                "layer0": "${modid}:blocks/${data.texture}"
            </#if>
        </#if>
    }
}
<#-- @formatter:on -->