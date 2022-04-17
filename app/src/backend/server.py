from socket import *
from create_user_id import *
import json

def printUserDB(db):
        for k, v in db.items():
                print(k,v)

start = '\033[2;31;43m ### REQUEST START ### \033[0;0m'
end = '\033[2;31;43m ### REQUEST END ### \033[0;0m'
s = socket()
port = 8080
s.bind(('localhost',port))
s.listen()
print("Server online at: ", port)
connNum = 0

users_database = {}


while True:
        print("#########################\n",start)
        c, addr = s.accept()
        connNum+=1

        response = ""
        complete = False
        print('connection from :', addr, "Connection Number :", )

        data = c.recv(1024)
        print("Data: ", data.decode())
        d = json.loads(data.decode())
        
        op = d['op']
        user = d['username']

        # request format : {"op":"LOGIN", "username":<username>, "password":<password>}
        # check users_database for valid login credentials
        if op == "LOGIN":
                if users_database.get(user) is None:
                        printUserDB(users_database)
                        response = '{\"op\":\"ERROR\"}'
                        complete = True

                elif users_database.get(user) is not None:
                        uid = createUserID(user, d['password'])
                        check = users_database.get(user, d['password']) == uid

                        if check:
                                response = '{\"op\":\"LOGIN_TRUE\"}'

                        else:
                                printUserDB(users_database)
                                response = '{\"op\":\"ERROR\"}'

                complete = True
                
        elif op == "CREATE_USER":

                if users_database.get(user):
                        printUserDB(users_database)
                        response = '{\"op\":\"ERROR\"}'

                else:
                        pass_hash = createUserID(user, d['password'])
                        users_database[user] = pass_hash
                        printUserDB(users_database)
                        response = '{\"op\":\"USER_CREATED\"}'

                complete = True

        if complete:
                c.send(response.encode('utf-8'))
                print("Response Sent: ", response)

        print(end, "\n#########################")
        c.close()
