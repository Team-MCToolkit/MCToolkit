{
  "parent": "block/template_torch_wall",
  "textures": {
    <#if data.particleTexture?has_content>"particle": "${modid}:blocks/${data.particleTexture}",</#if>
    "torch": "${modid}:blocks${data.texture}"
  }
}