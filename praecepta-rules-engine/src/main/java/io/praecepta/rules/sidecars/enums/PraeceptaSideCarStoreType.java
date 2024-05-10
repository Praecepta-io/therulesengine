package io.praecepta.rules.sidecars.enums;
import io.praecepta.core.data.enums.IPraeceptaRequestStoreType;

public enum PraeceptaSideCarStoreType implements IPraeceptaRequestStoreType{

//	PAERSER_SIDECAR_TYEP("ParserSideCarType"), ENRICHER_SIDECAR_TYEP("EnricherSideCarType"), FORMATTER_SIDECAR_TYPE("FormatterSideCarType"),
	SIDCAR_TO_TRIGGER("SideCarToTrigger"), SIDCAR_INPUT("SideCarInput"), SIDCAR_OUTPUT("SideCarOutput")
	, SIDCAR_RUNTIME_CONFIG("sideCarRuntimeConfig"), PARENT_SIDECAR_HOLDER("parentSideCarHolder")
	, CURRENT_SIDECAR_HOLDER("currentSideCarHolder"), NEXT_SIDECAR_HOLDER("currentSideCarHolder")
	;
	
	private PraeceptaSideCarStoreType(String sideCarTypeName) {
		this.sideCarTypeName = sideCarTypeName;
	}
	
	private String sideCarTypeName;
	
	@Override
	public String getStoringAttributeName() {
		return sideCarTypeName;
	}
}
