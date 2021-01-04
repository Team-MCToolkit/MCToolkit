{
  "parent": "block/template_torch",
  "textures": {
    <#if data.particleTexture?has_content>"particle": "${modid}:blocks/${data.particleTexture}",</#if>
    "torch": "${modid}:blocks${data.texture}"
  }
}