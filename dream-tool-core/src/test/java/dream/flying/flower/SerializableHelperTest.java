package dream.flying.flower;

import java.util.HashMap;

import dream.flying.flower.lang.SerializableHelper;

public class SerializableHelperTest {

	public static void main(String[] args) {
		HashMap<String, Object> ut = new HashMap<String, Object>();
		ut.put("username", "shimingxy");
		ut.put("password", "test");
		ut.put("department", "我的部门");
		String hexString = SerializableHelper.serializeHex(ut);

		System.out.println("hexString " + hexString);
		System.out.println(hexString.length());
		HashMap<String, Object> u2 = SerializableHelper.deserializeHex(hexString);
		System.out.println("deserialize " + u2.toString());
		System.out.println(
				"{’id’:’be90f66d-95df-4daf-93c1-ece002542702’,’tid’:null,’tname’:null,’description’:null,’status’:0,’sortOrder’:0,’createdBy’:’admin’,’createdDate’:’2014-11-07 21:27:38’,’modifiedBy’:null,’modifiedDate’:null,’startDate’:null,’endDate’:null,’username’:’yyyyy’,’password’:’Pt3dCf6Zad9h3g7q/DI0e7jQ5evO2Jn+tk2TjtdJ0eY=’,’decipherable’:’yaOLYlcdjfF5hFOskBOOxQ==’,’sharedSecret’:null,’sharedCounter’:null,’userType’:’EMPLOYEE’,’windowsAccount’:null,’displayName’:’test’,’nickName’:null,’nameZHSpell’:’test’,’nameZHShortSpell’:’test’,’givenName’:null,’middleName’:null,’familyName’:null,’honorificPrefix’:null,’honorificSuffix’:null,’formattedName’:null,’married’:0,’gender’:1,’birthDate’:null,’idType’:0,’idCardNo’:null,’webSite’:null,’startWorkDate’:null,’authnType’:0,’email’:null,’emailVerified’:0,’mobile’:null,’mobileVerified’:0,’passwordQuestion’:null,’passwordAnswer’:null,’appLoginAuthnType’:0,’appLoginPassword’:null,’protectedApps’:null,’passwordLastSetTime’:’2014-11-07 21:27:38’,’badPasswordCount’:0,’unLockTime’:null,’isLocked’:0,’lastLoginTime’:null,’lastLogoffTime’:null,’passwordSetType’:0,’locale’:’zh_CN’,’timeZone’:’Asia/Shanghai’,’preferredLanguage’:’zh_CN’,’workCountry’:’CHN’,’workRegion’:null,’workLocality’:null,’workStreetAddress’:null,’workAddressFormatted’:null,’workEmail’:null,’workPhoneNumber’:null,’workPostalCode’:null,’workFax’:null,’homeCountry’:’CHN’,’homeRegion’:null,’homeLocality’:null,’homeStreetAddress’:null,’homeAddressFormatted’:null,’homeEmail’:null,’homePhoneNumber’:null,’homePostalCode’:null,’homeFax’:null,’employeeNumber’:null,’costCenter’:null,’organization’:null,’division’:null,’departmentId’:null,’department’:null,’jobTitle’:null,’jobLevel’:null,’managerId’:null,’manager’:null,’assistantId’:null,’assistant’:null,’entryDate’:null,’quitDate’:null,’ims’:’QQ:\r\nWeiXin:\r\nSinaWeibo:\r\nGtalk:\r\nYiXin:\r\nIMessage:\r\nSkype:\r\nYahoo:\r\nMSN:\r\nAim:\r\nICQ :\r\nXmpp :’,’extraAttribute’:null,’extraAttributeName’:null,’extraAttributeValue’:null,’online’:0,’ldapDn’:null}"
						.length());
	}
}