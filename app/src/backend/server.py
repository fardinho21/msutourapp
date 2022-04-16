from socket import *
import json
start = '\033[2;31;43m ### REQUEST START ### \033[0;0m'
end = '\033[2;31;43m ### REQUEST END ### \033[0;0m'
s = socket()
port = 8080
s.bind(('localhost',port))
s.listen()
print("Server online at: ", port)


users_database = dict()


while True:
        print("#########################\n",start)
        c, addr = s.accept()
        print('connection from :', addr)
        data = c.recv(1024)
        print("Data: ", data.decode())

        d = json.loads(data.decode())
        op = d['op']

        if op == "LOGIN":
                c.send('{\"op\":\"LOGIN_TRUE\"}'.encode('utf-8'))
                pass #send back login confirm
        elif op == "CREATEUSER":
                pass #send back createuser confirm

        print(end, "\n#########################")
        c.close()
