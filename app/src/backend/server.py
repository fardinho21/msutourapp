from socket import *
from create_user_id import *
import json

def printUserDB(db):
        for k, v in db.items():
                print(k,v)

def printLandmarkDB(db):
        for lm in db:
                print(lm)

start = '\033[2;31;43m ### REQUEST START ### \033[0;0m'
end = '\033[2;31;43m ### REQUEST END ### \033[0;0m'
s = socket()
port = 8080
s.bind(('localhost',port))
s.listen()
print("Server online at: ", port)
connNum = 0

users_database = {}
landmark_database = {}

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
        # response format : { "op":"LOGIN_TRUE","username":<user> , "userId":<uid> }
        # error format : { "op": "ERROR" }
        # check users_database for valid login credentials
        if op == "LOGIN":
                if users_database.get(user) is None:
                        printUserDB(users_database)
                        response = '{\"op\":\"ERROR\"}'

                elif users_database.get(user) is not None:
                        uid = createUserID(user, d['password'])
                        check = users_database.get(user, d['password']) == uid

                        if check:
                                response = '{\"op\":\"LOGIN_TRUE\",\"username\":\"' + user + '\",\"userId\":\"' + uid + '\"}'

                        else:
                                printUserDB(users_database)
                                response = '{\"op\":\"ERROR\"}'

                complete = True
        
        # request format : {"op":"CREATE_USER", "username":<username>, "password":<password> }
        # response format : { "op":"USER_CREATED", "username":<user>, "userId":<uid> }
        # error format : { "op": "ERROR" }
        # checks users_database for already existing user, then creates user.
        elif op == "CREATE_USER":

                if users_database.get(user):
                        printUserDB(users_database)
                        response = '{\"op\":\"ERROR\"}'

                else:
                        uid = createUserID(user, d['password'])
                        users_database[user] = uid
                        landmark_database[user] = []
                        printUserDB(users_database)
                        printLandmarkDB(landmark_database)
                        response = '{\"op\":\"USER_CREATED\",\"userId\":\"' + uid + '\"}'

        # request format : {"op":"CREATE_LANDMARK", "username":<username>, "uid":<uid>, "landmark":<landmark-json-string> }
        # response format : { "op":"LANDMARK_CREATED", "userId":<uid> }
        # error format : { "op": "ERROR" }
        # checks users_database for already existing landmark, then creates landmark.
        elif op == "CREATE_LANDMARK":
                #create a landmark
                pass

        # request format : {"op":"FETCH_LANDMARKS", "username":<username>, "uid":<uid> }
        # response format : { "op":"FETCHED_LANDMARK_COLLECTION", "userId":<uid>, "landmarks":<comma separated landmark-json-strings> }
        # error format : { "op": "ERROR" }
        # checks users_database for existing user, returns landmark array
        elif op == "FETCH_LANDMARKS":
                users_database
                #return the array of landmarks
                pass

        complete = True

        if complete:
                c.send(response.encode('utf-8'))
                print("Response Sent: ", response)

        print(end, "\n#########################")
        c.close()
