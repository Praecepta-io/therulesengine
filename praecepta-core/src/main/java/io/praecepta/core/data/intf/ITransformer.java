package io.praecepta.core.data.intf;

public interface ITransformer<FROM_ENTITY, TO_ENTITY> {
	TO_ENTITY transform(FROM_ENTITY fromEntity);
}
