import pandas as pd
import numpy as np
import os
from com.chaquo.python import Python

def strtolist(list):
    badlist=list.strip('][').split(', ')
    inlist=False
    betterlist=[]
    lastlist=[]
    shortlist=[]
    for i in badlist:
        try:
            betterlist.append(int(i))
        except:
            betterlist.append(i)
    for i in betterlist:

        if(type(i)!=int):
            if(not inlist):
                if(i[0]=="["):
                    shortlist.append(i[1:])
                    inlist=True
                else:
                    shortlist.append(i)
                    inlist=True
            else:
                shortlist.append(int(i[:-1]))
                lastlist.append(shortlist)
                shortlist=[]
                inlist=False
        else:
            shortlist.append(i)
    return lastlist
def baggast(list):
    max=0
    for i in list:
        if(len(i)>max):
            max=len(i)
    return max

def padding(list):
    baggasts=baggast(list)
    for i in list:
        if (len(i)<baggasts):
            diff=baggasts-len(i)
            for j in range(diff):
                i.append(-100)
    return list

def headersfix(list):
    headerslist=[]
    results=[]
    for i in list:
        headerslist.append(i.pop(0))
    results=np.array(list).T.tolist()
    results.insert(0,headerslist)
    return results
def main(list,filename):
    os.chdir(os.environ["HOME"])
    f = open("demofile2.txt", "a")
    f.write("Now the file has more content!")
    f.close()
    list=strtolist(list)
    list=padding(list)
    readylist=headersfix(list)
    df = pd.DataFrame(readylist[1:-1],columns=readylist[0])

    files_dir = str(Python.getPlatform().getApplication().getExternalFilesDir(None))
    os.chdir(files_dir)
    df.to_csv(filename+".csv",index=False)
    return files_dir


