#include <nsIGenericFactory.h>
#include <nsICategoryManager.h>
#include <nsServiceManagerUtils.h>
#include <nsIClassInfoImpl.h>

#include <nsIScriptNameSpaceManager.h>

#include "clGreenFox.h"

NS_GENERIC_FACTORY_CONSTRUCTOR(clGreenFox)
NS_DECL_CLASSINFO(clGreenFox)


static NS_METHOD
registerSelf(nsIComponentManager *aCompMgr,
               nsIFile *aPath,
               const char *registryLocation,
               const char *componentType,
               const nsModuleComponentInfo *info)
{
    nsresult rv;
    nsCOMPtr<nsICategoryManager> catMan(do_GetService(NS_CATEGORYMANAGER_CONTRACTID, &rv));
    if (NS_FAILED(rv))
        return rv;

    catMan->AddCategoryEntry(JAVASCRIPT_GLOBAL_PROPERTY_CATEGORY,
                             "greenfox",
                             CL_GREENFOX_CONTRACT_ID,
                             PR_TRUE, PR_TRUE,
                             nsnull);
    return NS_OK;
}


static nsModuleComponentInfo components[] =
{
    {
       "Green Fox",				// * @param mDescription           : Class Name of given object	
       CL_GREENFOX_CID,			// * @param mCID                   : CID of given object
       CL_GREENFOX_CONTRACT_ID, // * @param mContractID            : Contract ID of given object
       clGreenFoxConstructor,	// * @param mConstructor           : Constructor of given object
       registerSelf,			// * @param mRegisterSelfProc      : (optional) Registration Callback
       NULL,					// * @param mUnregisterSelfProc    : (optional) Unregistration Callback
       NULL,					// * @param mFactoryDestructor     : (optional) Destruction Callback
       NS_CI_INTERFACE_GETTER_NAME(clGreenFox), // * @param mGetInterfacesProc     : (optional) Interfaces Callback
       NULL,					// * @param mGetLanguageHelperProc : (optional) Language Helper Callback
       &NS_CLASSINFO_NAME(clGreenFox), // * @param mClassInfoGlobal       : (optional) Global Class Info of given object 
	   NULL						// * @param mFlags                 : (optional) Class Info Flags @see nsIClassInfo 
    }
};

NS_IMPL_NSGETMODULE(clGreenFoxModule, components);
