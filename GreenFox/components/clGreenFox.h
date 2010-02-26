#ifndef __CL_GREENFOX_H__
#define __CL_GREENFOX_H__

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif /* HAVE_CONFIG_H */
#include "clIGreenFox.h"

#include <nsISecurityCheckedComponent.h>

// #define CL_CPU_CONTRACT_ID "@clear-code.com/system/cpu;1"
#define CL_CPU_CID {0x7465c6a6, 0xaa0d, 0x42b6, {0xb4, 0x44, 0x95, 0x24, 0xb7, 0x95, 0xd5, 0x0e}}

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

#endif /* __CL_GREENFOX_H__ */
