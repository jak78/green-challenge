var description = 'CPU component tests'

var gCPU;
var gCPUTime;

function setUp() {
}
                                                                     function MyComponentTestGo() {
	try {
		netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
		const cid = "@mydomain.com/XPCOMSample/MyComponent;1";
		obj = Components.classes[cid].createInstance();
		obj = obj.QueryInterface(Components.interfaces.IMyComponent);
	} catch (err) {
		alert(err);
		return;
	}
	var res = obj.Add(3, 4);
	alert('Performing 3+4. Returned ' + res + '.');
}

function tearDown() {
}

testCreate.description = "create instance test";
testCreate.priority = 'must';
function testCreate() {
  gCPU = Cc["@clear-code.com/system/cpu;1"].getService(Ci.clICPU);
  assert.isDefined(gCPU);
}

testUsage.description = "usage property test";
testUsage.priority = 'must';
function testUsage() {
  testCreate();
  assert.isDefined(gCPU.usage);
  assert.isNumber(gCPU.usage);
}

testGetCurrentTime.description = "user property test";
testGetCurrentTime.priority = 'must';
function testGetCurrentTime() {
  testCreate();
  gCPUTime = gCPU.getCurrentTime();
  assert.isDefined(gCPUTime);
}

testUser.description = "user property test";
testUser.priority = 'must';
function testUser() {
  testGetCurrentTime();
  assert.isNumber(gCPUTime.user);
}

testSystem.description = "system property test";
testSystem.priority = 'must';
function testSystem() {
  testGetCurrentTime();
  assert.isNumber(gCPUTime.system);
}

testIdle.description = "idle property test";
testIdle.priority = 'must';
function testIdle() {
  testGetCurrentTime();
  assert.isNumber(gCPUTime.idle);
}

