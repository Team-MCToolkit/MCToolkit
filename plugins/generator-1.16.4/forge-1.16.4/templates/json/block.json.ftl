{
    "parent": "block/${var_model}",
    "textures": {
        <#if data.texture.endsWith(".png")>
        "${var_txname}": "${data.texture.replace(".png", "")}",
        <#else>
        "${var_txname}": "${modid}:blocks/${data.texture}",
        </#if>

        <#if data.particleTexture?has_content>
            <#if data.particleTexture.endsWith(".png")>
            "particle": "${data.particleTexture.replace(".png", "")}"
            <#else>
            "particle": "${modid}:blocks/data.particleTexture"
            </#if>
        <#else>
            <#if data.texture.endsWith(".png")>
            "particle": "${data.texture.replace(".png", "")}"
            <#else>
            "particle": "${modid}:blocks/data.texture"
            </#if>
        </#if>
    }
}