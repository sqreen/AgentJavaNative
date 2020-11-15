#ifndef COMMON_H
#define COMMON_H

#include <jni.h>
#include "java_call.h"

#define JNI(method, ...) (*env)->method(env, ##__VA_ARGS__)
#ifdef UNUSED
#  undef UNUSED
#endif
#define UNUSED(var) (void) var

extern jclass jcls_rte;
extern jclass jcls_iae;
extern jmethodID rte_constr_cause;

extern jclass string_cls;
extern struct j_method to_string;

extern struct j_method number_longValue;
extern struct j_method number_doubleValue;
// weak, but assumed never to be gced
extern jclass *number_cls;

extern struct j_method map_entryset;
// weak, but assumed never to be gced
extern jclass *map_cls;
extern struct j_method map_size;
extern struct j_method entry_key;
extern struct j_method entry_value;

extern struct j_method iterable_iterator;
extern jclass *iterable_cls;
extern struct j_method iterator_next;
extern struct j_method iterator_hasNext;

extern struct j_method class_is_array;

#endif
