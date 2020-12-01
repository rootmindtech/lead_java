package com.rootmind.wrapper;

public class FileUploadWrapper extends AbstractWrapper{
	
		public String fileRefNo=null;
		public String fileTemplateID=null;  
		public String fileTemplateName=null;
		public String dataFileName=null;
		public String dataFileFolder=null;
		public String dataFileDateTime=null;
		public String makerID =null;
		public String makerDateTime=null;
		public String modifierId =null;
		public String modifierDateTime=null;
		
		//public String fileStatus=null;
		//public String uploadUserId=null;
		//public String uploadDateTime=null;
		//public String modifiedUserId=null;
		//public String modifiedDateTime=null;
		public boolean fileUploadStatus=false;
		public boolean fileFoundStatus=false;
		public boolean fileDeleteStatus=false;
		
		public String fileOperation=null;
		public String fileSeperator=null;
		public String sourcePath=null;
		public String destinationPath=null;
		public String backupPath=null;
		//public String docName=null;
		//public String docID=null;
		//public String docIDValue=null;
		
		public boolean invalidFileFormat=false;
		
		public boolean recordFound=false;

}
