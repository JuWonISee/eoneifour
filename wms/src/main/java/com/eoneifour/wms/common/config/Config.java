package com.eoneifour.wms.common.config;

//WMS에서 사용되는 상수를 관리하는 페이지
public class Config {
	public static final String[] MENUNAME = { "관리자", "입/출고", "입/출고기록", "재고 수정", "입고율 조회", "입고 조회", "출고 조회", "모니터링" };
	public static final String[] MENUKYES = { "ADMIN", "IO_BOUND", "IO_LOG", "STOCK_MODIFY", "INBOUND_RATE", "INBOUND", "OUTBOUND",
			"MONITORING" };
	
	public static final String[][] PAGENAME = { { "관리자 가입", "관리자 정보 수정", "회원 탈퇴" }, { "입고 지시", "출고 지시", "제품 조회" },
			{ "입고 기록", "출고 기록" }, { "재고 수정" }, { "전체 입고율", "스태커 입고율" },
			{ "렉별 입고상태 조회", "제품별 입고 조회", "날짜별 입고 조회", "입고 물품 중 주문 취소 조회", "제품별 출고 조회" },
			{ "날짜별 출고 조회", "창고 모니터링", "입고상태 동기화" } };

	public static final String[][] PAGEKEYS = { { "ADMIN_REGISTER", "ADMIN_EDIT", "ADMIN_DELETE" },
			{ "INBOUND_ORDER", "OUTBOUND_ORDER", "PRODUCT_LOOKUP" }, { "INBOUND_HISTORY", "OUTBOUND_HISTORY" },
			{ "INVENTORY_UPDATE" }, { "ALL_INBOUND_RATE", "STACKER_INBOUND_RATE" }, { "RACK_INBOUND_STATUS",
					"INBOUND_BY_PRODUCT", "INBOUND_BY_DATE", "CANCELLED_INBOUND_ORDERS", "OUTBOUND_BY_PRODUCT" },
			{ "OUTBOUND_BY_DATE", "MONITORING", "INBOUND_SYNC" } };
}
