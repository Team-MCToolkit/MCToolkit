<#include "mcitems.ftl">
/*@BlockState*/(new Object() {
	public BlockState cycle(BlockState _bs, String property, boolean forward) {
		try {
			IProperty _prop = _bs.getBlock().getStateContainer().getProperty(property);
			Collection _values = _prop.getAllowedValues();
			return _bs.with(_prop, forward ? Util.getElementAfter(_values, _bs.get(_prop)) : Util.getElementBefore(_values, _bs.get(_prop)));
		} catch (Exception e) {
			return _bs;
		}
}}.cycle(${mappedBlockToBlockStateCode(input$block)}, ${input$property}, ${input$forward}))