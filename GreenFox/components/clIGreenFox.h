/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM clIGreenFox.idl
 */

#ifndef __gen_clIGreenFox_h__
#define __gen_clIGreenFox_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif

/* starting interface:    clIGreenFox */
#define CLIGREENFOX_IID_STR "03a6d0b4-22b9-11df-b844-20d556d89593"

#define CLIGREENFOX_IID \
  {0x03a6d0b4, 0x22b9, 0x11df, \
    { 0xb8, 0x44, 0x20, 0xd5, 0x56, 0xd8, 0x95, 0x93 }}

class NS_NO_VTABLE NS_SCRIPTABLE clIGreenFox : public nsISupports {
 public: 

  NS_DECLARE_STATIC_IID_ACCESSOR(CLIGREENFOX_IID)

  /* void start (); */
  NS_SCRIPTABLE NS_IMETHOD Start(void) = 0;

  /* double stop (); */
  NS_SCRIPTABLE NS_IMETHOD Stop(double *_retval NS_OUTPARAM) = 0;

};

  NS_DEFINE_STATIC_IID_ACCESSOR(clIGreenFox, CLIGREENFOX_IID)

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_CLIGREENFOX \
  NS_SCRIPTABLE NS_IMETHOD Start(void); \
  NS_SCRIPTABLE NS_IMETHOD Stop(double *_retval NS_OUTPARAM); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_CLIGREENFOX(_to) \
  NS_SCRIPTABLE NS_IMETHOD Start(void) { return _to Start(); } \
  NS_SCRIPTABLE NS_IMETHOD Stop(double *_retval NS_OUTPARAM) { return _to Stop(_retval); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_CLIGREENFOX(_to) \
  NS_SCRIPTABLE NS_IMETHOD Start(void) { return !_to ? NS_ERROR_NULL_POINTER : _to->Start(); } \
  NS_SCRIPTABLE NS_IMETHOD Stop(double *_retval NS_OUTPARAM) { return !_to ? NS_ERROR_NULL_POINTER : _to->Stop(_retval); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class clGreenFox : public clIGreenFox
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_CLIGREENFOX

  clGreenFox();

private:
  ~clGreenFox();

protected:
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(clGreenFox, clIGreenFox)

clGreenFox::clGreenFox()
{
  /* member initializers and constructor code */
}

clGreenFox::~clGreenFox()
{
  /* destructor code */
}

/* void start (); */
NS_IMETHODIMP clGreenFox::Start()
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* double stop (); */
NS_IMETHODIMP clGreenFox::Stop(double *_retval NS_OUTPARAM)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_clIGreenFox_h__ */
