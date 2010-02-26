/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM clISystem.idl
 */

#ifndef __gen_clISystem_h__
#define __gen_clISystem_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

#ifndef __gen_nsIVariant_h__
#include "nsIVariant.h"
#endif

#ifndef __gen_clICPU_h__
#include "clICPU.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif

/* starting interface:    clISystemMonitor */
#define CLISYSTEMMONITOR_IID_STR "f2d40ab4-7d4a-4626-a0b6-e6c8acc6f170"

#define CLISYSTEMMONITOR_IID \
  {0xf2d40ab4, 0x7d4a, 0x4626, \
    { 0xa0, 0xb6, 0xe6, 0xc8, 0xac, 0xc6, 0xf1, 0x70 }}

class NS_NO_VTABLE NS_SCRIPTABLE clISystemMonitor : public nsISupports {
 public: 

  NS_DECLARE_STATIC_IID_ACCESSOR(CLISYSTEMMONITOR_IID)

  /* void monitor (in nsIVariant value); */
  NS_SCRIPTABLE NS_IMETHOD Monitor(nsIVariant *value) = 0;

};

  NS_DEFINE_STATIC_IID_ACCESSOR(clISystemMonitor, CLISYSTEMMONITOR_IID)

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_CLISYSTEMMONITOR \
  NS_SCRIPTABLE NS_IMETHOD Monitor(nsIVariant *value); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_CLISYSTEMMONITOR(_to) \
  NS_SCRIPTABLE NS_IMETHOD Monitor(nsIVariant *value) { return _to Monitor(value); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_CLISYSTEMMONITOR(_to) \
  NS_SCRIPTABLE NS_IMETHOD Monitor(nsIVariant *value) { return !_to ? NS_ERROR_NULL_POINTER : _to->Monitor(value); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class clSystemMonitor : public clISystemMonitor
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_CLISYSTEMMONITOR

  clSystemMonitor();

private:
  ~clSystemMonitor();

protected:
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(clSystemMonitor, clISystemMonitor)

clSystemMonitor::clSystemMonitor()
{
  /* member initializers and constructor code */
}

clSystemMonitor::~clSystemMonitor()
{
  /* destructor code */
}

/* void monitor (in nsIVariant value); */
NS_IMETHODIMP clSystemMonitor::Monitor(nsIVariant *value)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


/* starting interface:    clISystem */
#define CLISYSTEM_IID_STR "4d62df13-deee-4d5a-a7d9-2cabe54c7930"

#define CLISYSTEM_IID \
  {0x4d62df13, 0xdeee, 0x4d5a, \
    { 0xa7, 0xd9, 0x2c, 0xab, 0xe5, 0x4c, 0x79, 0x30 }}

class NS_NO_VTABLE NS_SCRIPTABLE clISystem : public nsISupports {
 public: 

  NS_DECLARE_STATIC_IID_ACCESSOR(CLISYSTEM_IID)

  /* readonly attribute clICPU cpu; */
  NS_SCRIPTABLE NS_IMETHOD GetCpu(clICPU * *aCpu) = 0;

  /* void addMonitor (in AString aTopic, in clISystemMonitor aMonitor, in long aInterval); */
  NS_SCRIPTABLE NS_IMETHOD AddMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor, PRInt32 aInterval) = 0;

  /* void removeMonitor (in AString aTopic, in clISystemMonitor aMonitor); */
  NS_SCRIPTABLE NS_IMETHOD RemoveMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor) = 0;

};

  NS_DEFINE_STATIC_IID_ACCESSOR(clISystem, CLISYSTEM_IID)

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_CLISYSTEM \
  NS_SCRIPTABLE NS_IMETHOD GetCpu(clICPU * *aCpu); \
  NS_SCRIPTABLE NS_IMETHOD AddMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor, PRInt32 aInterval); \
  NS_SCRIPTABLE NS_IMETHOD RemoveMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_CLISYSTEM(_to) \
  NS_SCRIPTABLE NS_IMETHOD GetCpu(clICPU * *aCpu) { return _to GetCpu(aCpu); } \
  NS_SCRIPTABLE NS_IMETHOD AddMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor, PRInt32 aInterval) { return _to AddMonitor(aTopic, aMonitor, aInterval); } \
  NS_SCRIPTABLE NS_IMETHOD RemoveMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor) { return _to RemoveMonitor(aTopic, aMonitor); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_CLISYSTEM(_to) \
  NS_SCRIPTABLE NS_IMETHOD GetCpu(clICPU * *aCpu) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetCpu(aCpu); } \
  NS_SCRIPTABLE NS_IMETHOD AddMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor, PRInt32 aInterval) { return !_to ? NS_ERROR_NULL_POINTER : _to->AddMonitor(aTopic, aMonitor, aInterval); } \
  NS_SCRIPTABLE NS_IMETHOD RemoveMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor) { return !_to ? NS_ERROR_NULL_POINTER : _to->RemoveMonitor(aTopic, aMonitor); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class clSystem : public clISystem
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_CLISYSTEM

  clSystem();

private:
  ~clSystem();

protected:
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(clSystem, clISystem)

clSystem::clSystem()
{
  /* member initializers and constructor code */
}

clSystem::~clSystem()
{
  /* destructor code */
}

/* readonly attribute clICPU cpu; */
NS_IMETHODIMP clSystem::GetCpu(clICPU * *aCpu)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void addMonitor (in AString aTopic, in clISystemMonitor aMonitor, in long aInterval); */
NS_IMETHODIMP clSystem::AddMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor, PRInt32 aInterval)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void removeMonitor (in AString aTopic, in clISystemMonitor aMonitor); */
NS_IMETHODIMP clSystem::RemoveMonitor(const nsAString & aTopic, clISystemMonitor *aMonitor)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_clISystem_h__ */
