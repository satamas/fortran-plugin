# Copyright 2000-2017 JetBrains s.r.o.
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
# http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import gdb

_use_new_pretty_printers = True
try:
    import gdb.printing
except ImportError:
    _use_new_pretty_printers = False

def untypedef(type):
    if type.code == gdb.TYPE_CODE_TYPEDEF:
        return type.strip_typedefs()
    return type

class ArrayPrinter(object):
    """ 
    Work-around for the slow -var-list-children when called on a big arrays (e.g. int[100000])
    https://youtrack.jetbrains.com/issue/CPP-5673 Command timeout during debug with large arrays
    >10x speedup
    """

    def __init__(self, val, type):
        self.val = val
        self.type = type

    def to_string(self):
        return None

    def display_hint(self):
        return 'array'

    def children(self):
        return self.Iterator(self.val, self.type)

    class Iterator:
        def __init__(self, val, type):
            self.val = val
            try:
                # nonzero array start indexes for Fortran
                (start_index, end_index) = type.range()
                self.index = start_index - 1
                self.end_index = end_index + 1
            except:
                self.index = -1
                # typedefs should be stripped otherwise debugger reports the wrong size
                self.end_index = untypedef(type).sizeof / untypedef(type).target().sizeof

        def __iter__(self):
            return self
            
        # python 3
        def __next__(self):
            self.index += 1
            if self.index == self.end_index:
                raise StopIteration

            return ('[{0}]'.format(self.index), self.val[self.index])

        # python 2.7
        def next(self):
            return self.__next__()



class TestPrinter(object):
    def __init__(self, val):
        self.val = val

    def to_string(self):
        return 'Type: {0}'.format(self.val.type.strip_typedefs())

    def display_hint(self):
        return 'string'


def lookup(val):
    type = untypedef(val.type)

    if type.code == gdb.TYPE_CODE_ARRAY:
        return ArrayPrinter(val, type)

    return None


def register_default_printers(obj):
    global _use_new_pretty_printers
    if _use_new_pretty_printers:
        gdb.printing.register_pretty_printer(obj, lookup)
    else:
        if obj is None:
            obj = gdb
        obj.pretty_printers.append(lookup)
