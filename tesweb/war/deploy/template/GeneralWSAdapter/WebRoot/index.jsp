<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.dc.tes.gwsa.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	</head>

	<body>
		This is my JSP page.
		<br>
		测试 post 方式提交

		<form
			action="<%=request.getContextPath()%>/servlet/GeneralWebServiceAdapter"
			target="_blank" method="post">
			Post内容
			<textarea name="soapXML" rows="20" cols="110"> 
			
				<?xml   version='1.0'   encoding='utf-8'?>   
				  <SOAP-ENV:Envelope
					xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
					xmlns:xsd='http://www.w3.org/2001/XMLSchema'
					xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'
					xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/'>   
				   
				  <SOAP-ENV:Body>   
				  <SyncOrderRelationReq xmlns='http://10.1.2.122/misc/dsmp.xsd'>   
				  <Version>1.5.0</Version>   
				  <MsgType>SyncOrderRelationReq</MsgType>   
				  <Send_Address>   
				  <DeviceType>0</DeviceType>   
				  <DeviceID>0011</DeviceID>   
				  </Send_Address>   
				  <Dest_Address>   
				  <DeviceType>400</DeviceType>   
				  <DeviceID>0</DeviceID>   
				  </Dest_Address>   
				  <FeeUser_ID>   
				  <UserIDType>2</UserIDType>   
				  <MSISDN></MSISDN>   
				  <PseudoCode>00116000000286</PseudoCode>   
				  </FeeUser_ID>   
				  <DestUser_ID>   
				  <UserIDType>2</UserIDType>   
				  <MSISDN></MSISDN>   
				  <PseudoCode>00116000000286</PseudoCode>   
				  </DestUser_ID>   
				  <LinkID>SP</LinkID>   
				  <ActionID>1</ActionID>   
				  <ActionReasonID>1</ActionReasonID>   
				  <SPID>900562</SPID>   
				  <SPServiceID>04101040</SPServiceID>   
				  <AccessMode>5</AccessMode>   
				  <FeatureStr></FeatureStr>   
				  </SyncOrderRelationReq>   
				  </SOAP-ENV:Body>   
				  </SOAP-ENV:Envelope>   
				
			</textarea>

			<input type="submit" value="提交" />
		</form>
	</body>
</html>
