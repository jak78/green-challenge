/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM clICPU.idl
 */

#ifndef __gen_clICPU_h__
#define __gen_clICPU_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

#ifndef __gen_clICPUTime_h__
#include "clICPUTime.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif

/* starting interface:    clICPU */
#define CLICPU_IID_STR "c98bfcd7-fa23-4d81-97a5-59eff0b2bacc"

#define CLICPU_IID \
  {0xc98bfcd7, 0xfa23, 0x4d81, \
    { 0x97, 0xa5, 0x59, 0xef, 0xf0, 0xb2, 0xba, 0xcc }}

class NS_NO_VTABLE NS_SCRIPTABLE clICPU : public nsISupports {
 public: 

  NS_DECLARE_STATIC_IID_ACCESSOR(CLICPU_IID)

  /* clICPUTime getCurrentTime (); */
  NS_SCRIPTABLE NS_IMETHOD GetCurrentTime(clICPUTime **_retval NS_OUTPARAM) = 0;

  /* readonly attribute double usage; */
  NS_SCRIPTABLE NS_IMETHOD GetUsage(double *aUsage) = 0;

};

  NS_DEFINE_STATIC_IID_ACCESSOR(clICPU, CLICPU_IID)

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_CLICPU \
  NS_SCRIPTABLE NS_IMETHOD GetCurrentTime(clICPUTime **_retval NS_OUTPARAM); \
  NS_SCRIPTABLE NS_IMETHOD GetUsage(double *aUsage); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_CLICPU(_to) \
  NS_SCRIPTABLE NS_IMETHOD GetCurrentTime(clICPUTime **_retval NS_OUTPARAM) { return _to GetCurrentTime(_retval); } \
  NS_SCRIPTABLE NS_IMETHOD GetUsage(double *aUsage) { return _to GetUsage(aUsage); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_CLICPU(_to) \
  NS_SCRIPTABLE NS_IMETHOD GetCurrentTime(clICPUTime **_retval NS_OUTPARAM) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetCurrentTime(_retval); } \
  NS_SCRIPTABLE NS_IMETHOD GetUsage(double *aUsage) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetUsage(aUsage); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class clCPU : public clICPU
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_CLICPU

  clCPU();

private:
  ~clCPU();

protected:
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(clCPU, clICPU)

clCPU::clCPU()
{
  /* member initializers and constructor code */
}

clCPU::~clCPU()
{
  /* destructor code */
}

/* clICPUTime getCurrentTime (); */
NS_IMETHODIMP clCPU::GetCurrentTime(clICPUTime **_retval NS_OUTPARAM)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* readonly attribute double usage; */
NS_IMETHODIMP clCPU::GetUsage(double *aUsage)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_clICPU_h__ */
