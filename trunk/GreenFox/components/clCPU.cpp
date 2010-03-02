#include "clCPU.h"

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

#include "clCPUTime.h"

clCPU::clCPU()
    : mPreviousUserTime(0)
    , mPreviousNiceTime(0)
    , mPreviousSystemTime(0)
    , mPreviousIdleTime(0)
    , mPreviousIOWaitTime(0)
{
#ifdef defined(XP_LINUX)
	tms ff;
		times(&ff);
		mPreviousUserTime = ff->tms_utime;
    mPreviousSystemTime = ff->tms_stime;
    mPreviousNiceTime = 0;
    mPreviousIdleTime = 0;
    mPreviousIOWaitTime = 0;
#elif defined(XP_WIN)

    FILETIME idleTime, kernelTime, userTime, creationTime, exitTime;

	HANDLE process;
	process = GetCurrentProcess();
	GetProcessTimes(process, &creationTime, &exitTime, &kernelTime, &userTime);

    mPreviousUserTime = FILETIME_TO_UINT64(userTime);
    mPreviousSystemTime = FILETIME_TO_UINT64(kernelTime);
    mPreviousIdleTime = 0;
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

clCPU::~clCPU()
{
}

NS_IMPL_ISUPPORTS2_CI(clCPU,
                      clICPU,
                      nsISecurityCheckedComponent)

NS_IMETHODIMP
clCPU::GetCurrentTime(clICPUTime **result NS_OUTPARAM)
{
#ifdef defined(XP_LINUX)
	glibtop_cpu cpu;
    glibtop_get_cpu(&cpu);

    guint64 user = cpu.user - mPreviousUserTime;
    guint64 system = cpu.sys - mPreviousSystemTime;
    guint64 nice = cpu.nice - mPreviousNiceTime;
    guint64 idle = cpu.idle - mPreviousIdleTime;
    guint64 io_wait = cpu.iowait + cpu.irq + cpu.softirq - mPreviousIOWaitTime;

    guint64 total = user + system + nice + idle + io_wait;

    if (total == 0) {
        *result = new clCPUTime(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    } else {
        *result = new clCPUTime((double)user / total,
                                (double)nice / total,
                                (double)system / total,
                                (double)idle / total,
                                (double)io_wait / total);
    }
    NS_ADDREF(*result);
    setPreviousCPUTime(&cpu);
    return NS_OK;
#elif defined(XP_WIN)

    FILETIME kernelTime, userTime, creationTime, exitTime;

	HANDLE process;
	process = GetCurrentProcess();
	GetProcessTimes(process, &creationTime, &exitTime, &kernelTime, &userTime);

    UINT64 user = FILETIME_TO_UINT64(userTime) - mPreviousUserTime;
    UINT64 kernel = FILETIME_TO_UINT64(kernelTime) - mPreviousSystemTime;
    UINT64 idle = 0; 

    UINT64 total = user + kernel;

    mPreviousUserTime = FILETIME_TO_UINT64(userTime);
    mPreviousSystemTime = FILETIME_TO_UINT64(kernelTime);
    mPreviousIdleTime = 0;

    if (total == 0) {
        *result = new clCPUTime(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    } else {
        *result = new clCPUTime((double)user / total,
                                (double)0.0f,
                                (double)kernel / total,
                                (double)0.0f,
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

NS_IMETHODIMP
clCPU::GetUsage(double *aUsage)
{
    nsCOMPtr<clICPUTime> cpuTime;
    nsresult rv = GetCurrentTime(getter_AddRefs(cpuTime));
    NS_ENSURE_SUCCESS(rv, rv);

    double user, system;
    cpuTime->GetUser(&user);
    cpuTime->GetSystem(&system);
    *aUsage = user + system;

    return NS_OK;
}

static char *
cloneAllAccessString (void)
{
    static const char allAccessString[] = "allAccess";
    return (char*)nsMemory::Clone(allAccessString, sizeof(allAccessString));
}

NS_IMETHODIMP
clCPU::CanCreateWrapper(const nsIID * iid, char **_retval NS_OUTPARAM)
{
    *_retval = cloneAllAccessString();
    return NS_OK;
}

NS_IMETHODIMP
clCPU::CanCallMethod(const nsIID * iid, const PRUnichar *methodName, char **_retval NS_OUTPARAM)
{
    *_retval = cloneAllAccessString();
    return NS_OK;
}

NS_IMETHODIMP
clCPU::CanGetProperty(const nsIID * iid, const PRUnichar *propertyName, char **_retval NS_OUTPARAM)
{
    *_retval = cloneAllAccessString();
    return NS_OK;
}

NS_IMETHODIMP
clCPU::CanSetProperty(const nsIID * iid, const PRUnichar *propertyName, char **_retval NS_OUTPARAM)
{
    *_retval = cloneAllAccessString();
    return NS_OK;
}
