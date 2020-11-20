<#-- @formatter:off -->
(new Object() {
	public int getFluidTankCapacity(BlockPos pos, int tank) {
		AtomicInteger _retval = new AtomicInteger(0);
		TileEntity _ent = world.getTileEntity(pos);
		if (_ent != null)
			_ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, ${input$direction}).ifPresent(capability ->
				_retval.set(capability.getTankCapacity(tank)));
		return _retval.get();
	}
}.getFluidTankCapacity(new BlockPos((int)${input$x},(int)${input$y},(int)${input$z}), (int)${input$tank}))
<#-- @formatter:on -->