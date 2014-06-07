#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os

HI_LINE = 80
LO_LINE = 70
REC_PATH = '/var/spool/asterisk/monitor/'

def getFreeSpace():
    file = os.popen('df')
    lines = file.readlines()
    line = lines[2]
    while line.find('  ') != -1:
        line = line.replace('  ',' ')
    freespace = int(line.split(' ')[4][:-1])
    return freespace

def getFileList():
    flist = os.listdir(REC_PATH)
    flist.sort()
    return flist


if __name__ == '__main__':
    if getFreeSpace() >= HI_LINE:
        files = getFileList()
        for file in files:
            cmd = 'rm ' + REC_PATH + file + ' -rf'
            os.system(cmd)
            if getFreeSpace() <= LO_LINE:
                break