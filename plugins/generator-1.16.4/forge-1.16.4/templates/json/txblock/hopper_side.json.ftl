<#-- @formatter:off -->
{
    "ambientocclusion": false,
    "textures": {
         <#if data.particleTexture?has_content>"particle": "${modid}:blocks/${data.particleTexture}",
         <#else> "particle": "${modid}:blocks/${data.textureFront?has_content?then(data.textureFront, data.texture)}",</#if>
        "top": "${modid}:blocks/${data.textureTop?has_content?then(data.textureTop, data.texture)}",
        "side": "${modid}:blocks/${data.textureFront?has_content?then(data.textureFront, data.texture)}",
        "inside": "${modid}:blocks/${data.textureBack?has_content?then(data.textureRight, data.texture)}"
    },
    "elements": [
        {   "from": [ 0, 10, 0 ],
            "to": [ 16, 11, 16 ],
            "faces": {
                "down":  { "texture": "#side" },
                "up":    { "texture": "#inside", "cullface": "up" },
                "north": { "texture": "#side", "cullface": "north" },
                "south": { "texture": "#side", "cullface": "south" },
                "west":  { "texture": "#side", "cullface": "west" },
                "east":  { "texture": "#side", "cullface": "east" }
            }
        },
        {   "from": [ 0, 11, 0 ],
            "to": [ 2, 16, 16 ],
            "faces": {
                "up":    { "texture": "#top", "cullface": "up" },
                "north": { "texture": "#side", "cullface": "north" },
                "south": { "texture": "#side", "cullface": "south" },
                "west":  { "texture": "#side", "cullface": "west" },
                "east":  { "texture": "#side", "cullface": "up" }
            }
        },
        {   "from": [ 14, 11, 0 ],
            "to": [ 16, 16, 16 ],
            "faces": {
                "up":    { "texture": "#top", "cullface": "up" },
                "north": { "texture": "#side", "cullface": "north" },
                "south": { "texture": "#side", "cullface": "south" },
                "west":  { "texture": "#side", "cullface": "up" },
                "east":  { "texture": "#side", "cullface": "east" }
            }
        },
        {   "from": [ 2, 11, 0 ],
            "to": [ 14, 16, 2 ],
            "faces": {
                "up":    { "texture": "#top", "cullface": "up" },
                "north": { "texture": "#side", "cullface": "north" },
                "south": { "texture": "#side", "cullface": "up" }
            }
        },
        {   "from": [ 2, 11, 14 ],
            "to": [ 14, 16, 16 ],
            "faces": {
                "up":    { "texture": "#top", "cullface": "up" },
                "north": { "texture": "#side", "cullface": "up" },
                "south": { "texture": "#side", "cullface": "south" }
            }
        },
        {   "from": [ 4, 4, 4 ],
            "to": [ 12, 10, 12 ],
            "faces": {
                "down":  { "texture": "#side" },
                "north": { "texture": "#side" },
                "south": { "texture": "#side" },
                "west":  { "texture": "#side" },
                "east":  { "texture": "#side" }
            }
        },
        {   "from": [ 6, 4, 0 ],
            "to": [ 10, 8, 4 ],
            "faces": {
                "down":  { "texture": "#side" },
                "up":    { "texture": "#side" },
                "north": { "texture": "#side", "cullface": "north" },
                "west":  { "texture": "#side" },
                "east":  { "texture": "#side" }
            }
        }
    ]
}

<#-- @formatter:on -->