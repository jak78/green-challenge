/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM clICPUTime.idl
 */

#ifndef __gen_clICPUTime_h__
#define __gen_clICPUTime_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif

/* starting interface:    clICPUTime */
#define CLICPUTIME_IID_STR "f85b3699-7675-4767-983d-fec8e04eee49"

#define CLICPUTIME_IID \
  {0xf85b3699, 0x7675, 0x4767, \
    { 0x98, 0x3d, 0xfe, 0xc8, 0xe0, 0x4e, 0xee, 0x49 }}

class NS_NO_VTABLE NS_SCRIPTABLE clICPUTime : public nsISupports {
 public: 

  NS_DECLARE_STATIC_IID_ACCESSOR(CLICPUTIME_IID)

  /* readonly attribute double user; */
  NS_SCRIPTABLE NS_IMETHOD GetUser(double *aUser) = 0;

  /* readonly attribute double nice; */
  NS_SCRIPTABLE NS_IMETHOD GetNice(double *aNice) = 0;

  /* readonly attribute double system; */
  NS_SCRIPTABLE NS_IMETHOD GetSystem(double *aSystem) = 0;

  /* readonly attribute double idle; */
  NS_SCRIPTABLE NS_IMETHOD GetIdle(double *aIdle) = 0;

  /* readonly attribute double io_wait; */
  NS_SCRIPTABLE NS_IMETHOD GetIo_wait(double *aIo_wait) = 0;

};

  NS_DEFINE_STATIC_IID_ACCESSOR(clICPUTime, CLICPUTIME_IID)

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_CLICPUTIME \
  NS_SCRIPTABLE NS_IMETHOD GetUser(double *aUser); \
  NS_SCRIPTABLE NS_IMETHOD GetNice(double *aNice); \
  NS_SCRIPTABLE NS_IMETHOD GetSystem(double *aSystem); \
  NS_SCRIPTABLE NS_IMETHOD GetIdle(double *aIdle); \
  NS_SCRIPTABLE NS_IMETHOD GetIo_wait(double *aIo_wait); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_CLICPUTIME(_to) \
  NS_SCRIPTABLE NS_IMETHOD GetUser(double *aUser) { return _to GetUser(aUser); } \
  NS_SCRIPTABLE NS_IMETHOD GetNice(double *aNice) { return _to GetNice(aNice); } \
  NS_SCRIPTABLE NS_IMETHOD GetSystem(double *aSystem) { return _to GetSystem(aSystem); } \
  NS_SCRIPTABLE NS_IMETHOD GetIdle(double *aIdle) { return _to GetIdle(aIdle); } \
  NS_SCRIPTABLE NS_IMETHOD GetIo_wait(double *aIo_wait) { return _to GetIo_wait(aIo_wait); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_CLICPUTIME(_to) \
  NS_SCRIPTABLE NS_IMETHOD GetUser(double *aUser) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetUser(aUser); } \
  NS_SCRIPTABLE NS_IMETHOD GetNice(double *aNice) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetNice(aNice); } \
  NS_SCRIPTABLE NS_IMETHOD GetSystem(double *aSystem) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetSystem(aSystem); } \
  NS_SCRIPTABLE NS_IMETHOD GetIdle(double *aIdle) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetIdle(aIdle); } \
  NS_SCRIPTABLE NS_IMETHOD GetIo_wait(double *aIo_wait) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetIo_wait(aIo_wait); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class clCPUTime : public clICPUTime
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_CLICPUTIME

  clCPUTime();

private:
  ~clCPUTime();

protected:
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(clCPUTime, clICPUTime)

clCPUTime::clCPUTime()
{
  /* member initializers and constructor code */
}

clCPUTime::~clCPUTime()
{
  /* destructor code */
}

/* readonly attribute double user; */
NS_IMETHODIMP clCPUTime::GetUser(double *aUser)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* readonly attribute double nice; */
NS_IMETHODIMP clCPUTime::GetNice(double *aNice)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* readonly attribute double system; */
NS_IMETHODIMP clCPUTime::GetSystem(double *aSystem)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* readonly attribute double idle; */
NS_IMETHODIMP clCPUTime::GetIdle(double *aIdle)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* readonly attribute double io_wait; */
NS_IMETHODIMP clCPUTime::GetIo_wait(double *aIo_wait)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_clICPUTime_h__ */
