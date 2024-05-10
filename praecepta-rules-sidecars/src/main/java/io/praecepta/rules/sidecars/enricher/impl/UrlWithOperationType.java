package io.praecepta.rules.sidecars.enricher.impl;

import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType;
import io.praecepta.rules.sidecars.enricher.IPraeceptaSideCarEnricher;

public class UrlWithOperationType{
		String url;
		
		PraeceptaWsOperationType httpRequestType;
		
		IPraeceptaSideCarEnricher outerType;
		
		public UrlWithOperationType(String url, PraeceptaWsOperationType httpRequestType,IPraeceptaSideCarEnricher outerType) {
			this.url = url;
			this.httpRequestType = httpRequestType;
			this.outerType=outerType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((httpRequestType == null) ? 0 : httpRequestType.hashCode());
			result = prime * result + ((url == null) ? 0 : url.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UrlWithOperationType other = (UrlWithOperationType) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (httpRequestType != other.httpRequestType)
				return false;
			if (url == null) {
				if (other.url != null)
					return false;
			} else if (!url.equals(other.url))
				return false;
			return true;
		}

		private IPraeceptaSideCarEnricher getOuterType() {
			return this.outerType;
		}
	}