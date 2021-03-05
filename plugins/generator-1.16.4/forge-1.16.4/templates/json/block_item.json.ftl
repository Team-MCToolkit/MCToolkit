<#-- @formatter:off -->
<#if data.itemTexture?has_content>
{
  "parent": "item/generated",
  "textures": {
    <#if data.itemTexture.endsWith(".png")>
    "layer0": "${data.itemTexture.replace(".png", "")}"
    <#else>
    "layer0": "${modid}:items/${data.itemTexture}"
    </#if>
  }
}
<#else>
{
    <#if data.texture.endsWith(".png")>
    "parent": "${data.texture.replace(".png", "")}",
    <#else>
    "parent": "${modid}:block/${registryname}",
    </#if>
    "display": {
      "thirdperson": {
        "rotation": [
          10,
          -45,
          170
        ],
        "translation": [
          0,
          1.5,
          -2.75
        ],
        "scale": [
          0.375,
          0.375,
          0.375
        ]
      }
    }
}
</#if>
<#-- @formatter:on -->