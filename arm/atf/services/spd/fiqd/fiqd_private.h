/*
 * Copyright (c) 2013-2014, ARM Limited and Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of ARM nor the names of its contributors may be used
 * to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

#ifndef __FIQD_PRIVATE_H__
#define __FIQD_PRIVATE_H__

#include <arch.h>
#include <context.h>
#include <interrupt_mgmt.h>
#include <platform_def.h>
#include <psci.h>

/*******************************************************************************
 * Secure Payload PM state information e.g. SP is suspended, uninitialised etc
 * and macros to access the state information in the per-cpu 'state' flags
 ******************************************************************************/
#define TSP_PSTATE_OFF		0
#define TSP_PSTATE_ON		1
#define TSP_PSTATE_SUSPEND	2
#define TSP_PSTATE_SHIFT	0
#define TSP_PSTATE_MASK	0x3
#define get_tsp_pstate(state)	((state >> TSP_PSTATE_SHIFT) & TSP_PSTATE_MASK)
#define clr_tsp_pstate(state)	(state &= ~(TSP_PSTATE_MASK \
					    << TSP_PSTATE_SHIFT))
#define set_tsp_pstate(st, pst)	do {					       \
					clr_tsp_pstate(st);		       \
					st |= (pst & TSP_PSTATE_MASK) <<       \
						TSP_PSTATE_SHIFT;	       \
				} while (0);


/*
 * This flag is used by the TSPD to determine if the TSP is servicing a standard
 * SMC request prior to programming the next entry into the TSP e.g. if TSP
 * execution is preempted by a non-secure interrupt and handed control to the
 * normal world. If another request which is distinct from what the TSP was
 * previously doing arrives, then this flag will be help the TSPD to either
 * reject the new request or service it while ensuring that the previous context
 * is not corrupted.
 */
#define STD_SMC_ACTIVE_FLAG_SHIFT	2
#define STD_SMC_ACTIVE_FLAG_MASK	1
#define get_std_smc_active_flag(state)	((state >> STD_SMC_ACTIVE_FLAG_SHIFT) \
					 & STD_SMC_ACTIVE_FLAG_MASK)
#define set_std_smc_active_flag(state)	(state |=                             \
					 1 << STD_SMC_ACTIVE_FLAG_SHIFT)
#define clr_std_smc_active_flag(state)	(state &=                             \
					 ~(STD_SMC_ACTIVE_FLAG_MASK           \
					   << STD_SMC_ACTIVE_FLAG_SHIFT))

/*******************************************************************************
 * Secure Payload execution state information i.e. aarch32 or aarch64
 ******************************************************************************/
#define TSP_AARCH32		MODE_RW_32
#define TSP_AARCH64		MODE_RW_64


/*******************************************************************************
 * Number of cpus that the present on this platform. TODO: Rely on a topology
 * tree to determine this in the future to avoid assumptions about mpidr
 * allocation
 ******************************************************************************/
#define FIQD_CORE_COUNT		PLATFORM_CORE_COUNT

#ifndef __ASSEMBLY__

#include <cassert.h>
#include <stdint.h>

/*
 * The number of arguments to save during a SMC call for FIQ.
 * Currently only x1 and x2 are used by FIQ.
 */
#define FIQD_NUM_ARGS	0x2

#if 0
/* AArch64 callee saved general purpose register context structure. */
DEFINE_REG_STRUCT(c_rt_regs, FIQD_C_RT_CTX_ENTRIES);

/*
 * Compile time assertion to ensure that both the compiler and linker
 * have the same double word aligned view of the size of the C runtime
 * register context.
 */
CASSERT(FIQD_C_RT_CTX_SIZE == sizeof(c_rt_regs_t),	\
	assert_spd_c_rt_regs_size_mismatch);
#endif

/*******************************************************************************
 * Structure which helps the SPD to maintain the per-cpu state of the SP.
 * 'saved_spsr_el3' - temporary copy to allow FIQ handling when the FIQ has been
 *                    preempted.
 * 'saved_elr_el3'  - temporary copy to allow FIQ handling when the FIQ has been
 *                    preempted.
 * 'state'          - collection of flags to track SP state e.g. on/off
 * 'mpidr'          - mpidr to associate a context with a cpu
 * 'c_rt_ctx'       - stack address to restore C runtime context from after
 *                    returning from a synchronous entry into the SP.
 * 'cpu_ctx'        - space to maintain SP architectural state
 * 'saved_fiq_args' - space to store arguments for FIQ arithmetic operations
 *                    which will queried using the FIQ_GET_ARGS SMC by FIQ.
 ******************************************************************************/
typedef struct fiq_context {
	uint64_t saved_elr_el3;
	uint32_t saved_spsr_el3;
	uint32_t state;
	uint64_t mpidr;
	uint64_t c_rt_ctx;
	cpu_context_t cpu_ctx;
	uint64_t saved_fiq_args[FIQD_NUM_ARGS];
} fiq_context_t;

/*******************************************************************************
 * Function & Data prototypes
 ******************************************************************************/
int32_t fiqd_init_secure_context(uint64_t entrypoint,
					uint32_t rw,
					uint64_t mpidr,
					fiq_context_t *fiq_ctx);
extern fiq_context_t fiqd_sp_context[FIQD_CORE_COUNT];

#endif /*__ASSEMBLY__*/

#endif /* __FIQD_PRIVATE_H__ */
