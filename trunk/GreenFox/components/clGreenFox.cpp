#include "clGreenFox.h"

#include <nsIClassInfoImpl.h>
#include <nsMemory.h>
#include <nsCOMPtr.h>

#ifdef defined(XP_LINUX)
#include <sys/times.h>
#elif defined(XP_WIN)
#include <windows.h>
#undef GetCurrentTime /* CAUTION! Use GetTickCount instead of GetCurrentTime*/
#undef AddMonitor /* CAUTION! Use AddMonitorW instead */
#define FILETIME_TO_UINT64(v) (v.dwLowDateTime + ((UINT64)v.dwHighDateTime << 32))
#elif defined(XP_MACOSX)
#include <mach/mach_host.h>
#include <mach/vm_map.h>
#endif

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
#ifdef defined(XP_LINUX)
		times(&clGreenFox::start);
		return NS_OK;
    
#elif defined(XP_WIN)
    FILETIME idleTime, kernelTime, userTime;
    GetSystemTimes(&idleTime, &kernelTime, &userTime);
    mPreviousUserTime = FILETIME_TO_UINT64(userTime);
    mPreviousSystemTime = FILETIME_TO_UINT64(kernelTime);
    mPreviousIdleTime = FILETIME_TO_UINT64(idleTime);
    mPreviousNiceTime = 0;
    mPreviousIOWaitTime = 0;
#elif defined(XP_MACOSX)
    natural_t nProcessors;
    mach_msg_type_number_t nProcessorInfos;
    processor_cpu_load_info_data_t *processorInfos;

    if (host_processor_info(mach_host_self(),
                            PROCESSOR_CPU_LOAD_INFO,
                            &nProcessors,
                            (processor_info_array_t*)&processorInfos,
                            &nProcessorInfos)) {
        return;
    }

    for (unsigned int i = 0; i < nProcessors; i++) {
        mPreviousUserTime += processorInfos[i].cpu_ticks[CPU_STATE_USER];
        mPreviousNiceTime += processorInfos[i].cpu_ticks[CPU_STATE_NICE];
        mPreviousSystemTime += processorInfos[i].cpu_ticks[CPU_STATE_SYSTEM];
        mPreviousIdleTime += processorInfos[i].cpu_ticks[CPU_STATE_IDLE];
    }
#endif
}

/* double stop (); */
NS_IMETHODIMP clGreenFox::Stop(double *_retval NS_OUTPARAM)
{
#ifdef defined(XP_LINUX)
	tms stop;
	times(&stop);           
		
	clock_t user_time = stop->tms_utime - clGreenFox::start->tms_utime;
	clock_t system_time = stop->tms_stime - clGreenFox::start->tms_stime;
	clock_t user_children_time = stop->tms_cutime - clGreenFox::start->tms_cutime;
	clock_t system_children_time = stop->tms_cstime - clGreenFox::start->tms_cstime;
	
	_retval = user_time + system_time + user_children_time + system_children_time;
  return NS_OK;
#elif defined(XP_WIN)
    FILETIME idleTime, kernelTime, userTime;
    GetSystemTimes(&idleTime, &kernelTime, &userTime);

    UINT64 user = FILETIME_TO_UINT64(userTime) - mPreviousUserTime;
    UINT64 kernel = FILETIME_TO_UINT64(kernelTime) - mPreviousSystemTime;
    UINT64 idle = FILETIME_TO_UINT64(idleTime) - mPreviousIdleTime;

    UINT64 total = user + kernel;

    /*
      Trick!!
      On windows, we can not calcurate kernel and user times without idle time respectively,
      because the kernel and user times which returned by GetSystemTimes are including
      idle times.
      kernel time = (cpu usage time in kernel) + (cpu idle time in kernel)
      user time = (cpu usage time in user space) + (cpu idle time in user space)
      idle time = (cpu idle time in kernel) + (cpu idle time in user space)
      So we set (cpu usage time in kernel) + (cpu usage time in user space) value as
      kernel time for the convinience. This value is used in GetUsage.
    */
    kernel = total - idle;

    mPreviousUserTime = FILETIME_TO_UINT64(userTime);
    mPreviousSystemTime = FILETIME_TO_UINT64(kernelTime);
    mPreviousIdleTime = FILETIME_TO_UINT64(idleTime);

    if (total == 0) {
        *result = new clCPUTime(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    } else {
        *result = new clCPUTime((double)0.0f,
                                (double)0.0f,
                                (double)kernel / total,
                                (double)idle / total,
                                (double)0.0f);
    }
    NS_ADDREF(*result);

    return NS_OK;
#elif defined(XP_MACOSX)
    natural_t nProcessors;
    mach_msg_type_number_t nProcessorInfos;
    processor_cpu_load_info_data_t *processorInfos;

    if (host_processor_info(mach_host_self(),
                            PROCESSOR_CPU_LOAD_INFO,
                            &nProcessors,
                            (processor_info_array_t*)&processorInfos,
                            &nProcessorInfos)) {
        return NS_ERROR_FAILURE;
    }

    PRUint64 currentUser = 0;
    PRUint64 currentNice = 0;
    PRUint64 currentSystem = 0;
    PRUint64 currentIdle = 0;

    for (unsigned int i = 0; i < nProcessors; i++) {
        currentUser += processorInfos[i].cpu_ticks[CPU_STATE_USER];
        currentNice += processorInfos[i].cpu_ticks[CPU_STATE_NICE];
        currentSystem += processorInfos[i].cpu_ticks[CPU_STATE_SYSTEM];
        currentIdle += processorInfos[i].cpu_ticks[CPU_STATE_IDLE];
    }

    PRUint64 user, nice, system, idle, total;
    user = currentUser - mPreviousUserTime;
    nice = currentNice - mPreviousNiceTime;
    system = currentSystem - mPreviousSystemTime;
    idle = currentIdle - mPreviousIdleTime;

    mPreviousUserTime = currentUser;
    mPreviousNiceTime = currentNice;
    mPreviousSystemTime = currentSystem;
    mPreviousIdleTime = currentIdle;

    total = user + nice + system + idle;

    if (total == 0) {
        *result = new clCPUTime(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    } else {
        *result = new clCPUTime((double)user / total,
                                (double)nice / total,
                                (double)system / total,
                                (double)idle / total,
                                (double)0.0f);
    }
    NS_ADDREF(*result);

    return NS_OK;
#else
    return NS_ERROR_NOT_IMPLEMENTED;
#endif
}