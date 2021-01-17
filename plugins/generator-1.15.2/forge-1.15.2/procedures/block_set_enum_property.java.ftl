try {
	BlockState _bs =  world.getBlockState(new BlockPos((int)${input$x},(int)${input$y},(int)${input$z}));
	EnumProperty _prop = (EnumProperty) _bs.getBlock().getStateContainer().getProperty((${input$property}).toLowerCase(java.util.Locale.ENGLISH));
	world.setBlockState(new BlockPos((int)${input$x},(int)${input$y},(int)${input$z}),
		_bs.with(_prop, (Enum) _prop.parseValue((${input$value}).toLowerCase(java.util.Locale.ENGLISH)).get()), 3);
} catch (Exception e) {
}