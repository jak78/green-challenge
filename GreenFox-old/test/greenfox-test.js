netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
obj = Cc["@octo.com/green/fox;1"];
alert(obj);
greenFox = obj.getService(Ci.clGreenFox);
alert(greenFox);
greenFox.start();   

a = greenFox.stop();
alert(a);                


