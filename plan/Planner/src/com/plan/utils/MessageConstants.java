package com.plan.utils;

public final class MessageConstants {

	public static final class Messages {
		public static final String INVALID_VALUE = "Invalid Value";
		public static final String INVALID_USERNAME = "Invalid Username";
		public static final String INVALID_PASSWORD = "Invalid Password";
		public static final String SAVE_SUCCESS = "Data Saved Successfully";
		public static final String SAVE_FAILURE = "Data Could not be saved";
		public static final String VALUE_REQUIRED = "Value is required";
		public static final String MANDATORY_REQUIRED = "Mandatory fields are required";
		public static final String INVALID_DATE = "Date From should be less than Date To";
		public static final String UPDATE_SUCCESS = "Data has been updated successfully";
		public static final String UPDATE_FAILURE = "Data could not be updated";
		public static final String DELETE_SUCCESS = "Data has been deleted Successfully";
		public static final String DELETE_FAILURE = "Data could not be deleted";
		
		public static final String REQUIRED_CRITERIA = "You did not specify any search criteria";
		
	}

	public static final class Constants 
	{
		public static final String DISPLAY_UNIT = "Display";
		public static final String PASSWORD_KEY = "1";
		public static final String PROFILE_CURRENT = "Current";
		public static final String SELECT_ONE_STRING = "Select One";
		public static final String NA_STRING = "NA";
		public static final String YES_STRING = "Yes";
		public static final String NO_STRING = "No";
		public static final Integer AUTO_COMPLETE_SIZE = new Integer(30);		
		public static final Integer ID_OFFSET = new Integer(12345);
		
		public static final String NIL_STRING = "Nil";
		public static final String TRACE_STRING = "Trace";
		public static final String PRESENT_STRING = "Present";
		
		public static final String NEGATIVE_STRING = "Negative";
		public static final String POSITIVE_STRING = "Positive";
		public static final String WEAK_REACTIVE_STRING = "Weak Reactive";
		
		public static final String SEEN_STRING = "Seen";
		public static final String NOT_SEEN_STRING = "Not Seen";
		
		public static final String VERIFIED_STRING = "Verified";
		
		public static final Integer MINIMUM_INTERVAL = Integer.valueOf(Environment.getProperty("minimumInterval"));
		
		public static final Integer BLINK_TIME = Integer.valueOf(Environment.getProperty("blinkTime"));

		public static final class ClientStatus {
			
			public static final String REGISTERED = "Registered";
			public static final String REPEATER = "Repeat";
			public static final String FIT = "Fit";
			public static final String UNFIT = "Unfit";
			
		}
		
		

		
		public static final class PageTitles 
		{			
			public static final String HOME_PAGE = "Upcoming IHD - CMTs";
			public static final String ADD_PLAN = "Add New IHD / CMT";
			public static final String UPDATE_PLAN = "Update Existing IHD/CMT";
			public static final String UPLOAD_SCANNED = "Upload Scanned Files";
			
			public static final String RESET_REPEATER = "Reset Repeater Clients";
			
			public static final String CASH_PAYMENT = "Cash and Accounts";
			
			public static final String GPE_XRAY = "G.P.E";
			
			public static final String RADIOLOGY = "Radiology";
			
			public static final String SAMPLE_COLLECTION = "Sample Collection";
			public static final String LAB_MGMT = "Lab Mgmt.";
			
			public static final String PATHOLOGY = "Pathology";
			
			public static final String DIRECTOR = "Director's Declaration";
			
			public static final String PRINT_REPORT = "Print Report";
			
			public static final String ADV_SEARCH = "Advanced Search";
			
			public static final String ADMIN = "Admin Tasks";
			
			
		}
		
		public static final class ScannedFileTypes 
		{			
			public static final String GAMCA = "gamca";
			public static final String PASSPORT = "passport";
			public static final String PHOTO = "photo";			
		}
	
		public static final class GCCCountries 
		{
			public static final String BAHRAIN = "Bahrain";
			public static final String QATAR = "Qatar";
			public static final String KSA = "KSA";
			public static final String OMAN = "Oman";
			public static final String KUWAIT = "Kuwait";
		}

		public static final class LabMicro 
		{
			public static final String RANGE_1_2 = "1-2";
			public static final String RANGE_3_5 = "3-5";
			public static final String RANGE_5_10 = "5-10";		
		}
		
		public static final class CashPaymentStatus {
			public static final String PAID = "Paid";
			public static final String UNPAID = "Unpaid";			
		}

		public static final class BloodGps 
		{
			public static final String A_POS = "A Positive";
			public static final String B_POS = "B Positive";
			public static final String AB_POS = "AB Positive";
			public static final String O_POS = "O Positive";
			public static final String A_NEG = "A Negative";
			public static final String B_NEG = "B Negative";
			public static final String AB_NEG = "AB Negative";
			public static final String O_NEG = "O Negative";			
		}
		
		public static final class TrackActions 
		{
			public static final String PLAN_SAVED = "IHD/CMT Saved";
			public static final String PLAN_UPDATED = "IHD/CMT Updated";
			public static final String PLAN_DELETED = "IHD/CMT Deleted";
			
			public static final String SCANNED_UPDATED = "Scanned files Uploaded";
			
			public static final String CASH = "Cash payment updated";
			
			public static final String GPE = "GPE Status updated";
			
			public static final String XRAY = "Xray status updated";
			
			public static final String RADIOLOGY = "Radiologist updated status";
			
			public static final String SAMPLES = "Samples collected";
			
			public static final String LAB = "Lab manager updated values";
			
			public static final String PATHOLOGIST = "Pathologist updated status";
			
			public static final String PATHOLOGIST_FIT = "Pathologist updated status as Fit";
			
			public static final String PATHOLOGIST_UNFIT = "Pathologist updated status as Unfit";
			
			public static final String DIRECTOR = "Medical Director updated status";
			
			public static final String RESET_REPEATER = "Repeater declared as Registered";
			
			public static final String RESET_ADMIN_DIRECTOR = "Director status reset";
			
			public static final String RESET_ADMIN_PATHOLOGY = "Pathologist status reset";
			
			public static final String RESET_ADMIN_GPE = "GPE status reset";
			
			
		}
		
		public static final class ChartLabels 
		{
			public static final String REGN = "Registration";
			public static final String CASH = "Cash";
			public static final String GPE = "GPE";
			public static final String SAMPLES = "Samples";
			public static final String XRAY = "X-Ray";
			public static final String RADIOLOGIST = "Radiologist";
			public static final String LAB = "Lab";
			public static final String PATHOLOGIST = "Pathologist";
			public static final String DIRECTOR = "Director";
						
		}
	}

}
