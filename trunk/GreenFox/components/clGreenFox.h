#ifndef __CL_GREENFOX_H__
#define __CL_GREENFOX_H__

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif /* HAVE_CONFIG_H */
#include "clIGreenFox.h"

#ifdef defined(XP_LINUX)
#include <sys/times.h>
#endif

#include <nsISecurityCheckedComponent.h>

#define CL_GREENFOX_CONTRACT_ID "@octo.com/green/fox;1"
#define CL_GREENFOX_CLASSNAME "GreenFox"
#define CL_GREENFOX_CID {0x04e340ed, 0xec7e, 0x4e94, {0xaa, 0x64, 0xb9, 0xbb, 0x0c, 0x1c, 0xc5, 0x40}}
                                       
class clGreenFox : public clIGreenFox
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_CLIGREENFOX

  clGreenFox();

private:
  ~clGreenFox();

protected:
#ifdef defined(XP_LINUX)
	static tms start;
#endif

};

#endif /* __CL_GREENFOX_H__ */
