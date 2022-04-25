from socket import *
from utility import *
import json



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
        data_as_json = json.loads(data.decode())
        
        op = data_as_json['op']
        user = data_as_json['username']

        # request format : {"op":"LOGIN", "username":<username>, "password":<password>}
        # response format : { "op":"LOGIN_TRUE","username":<user> , "userId":<uid> }
        # error format : { "op": "ERROR" }
        # check users_database for valid login credentials
        if op == "LOGIN":
                if users_database.get(user) is None:
                        printUserDB(users_database)
                        #response = '{\"op\":\"ERROR\"}'
                        response = json.dumps({"op":"ERROR"})


                elif users_database.get(user) is not None:
                        uid = createUserID(user, data_as_json['password'])
                        check = users_database.get(user, data_as_json['password']) == uid

                        if check:
                                #response = '{\"op\":\"LOGIN_TRUE\",\"username\":\"' + user + '\",\"userId\":\"' + uid + '\"}'
                                response = json.dumps({"op":"LOGIN_TRUE","username":user,"userId":uid})

                        else:
                                printUserDB(users_database)
                                #response = '{\"op\":\"ERROR\"}'
                                response = json.dumps({"op":"ERROR"})

                complete = True
        
        # request format : {"op":"CREATE_USER", "username":<username>, "password":<password> }
        # response format : { "op":"USER_CREATED", "username":<user>, "userId":<uid> }
        # error format : { "op": "ERROR" }
        # checks users_database for already existing user, then creates user.
        elif op == "CREATE_USER":

                if users_database.get(user):
                        printUserDB(users_database)
                        #response = '{\"op\":\"ERROR\"}'
                        response = json.dumps({"op":"ERROR"})

                else:
                        uid = createUserID(user, data_as_json['password'])
                        users_database[user] = uid
                        landmark_database[user] = []
                        printUserDB(users_database)
                        printLandmarkDB(landmark_database)
                        #response = '{\"op\":\"USER_CREATED\",\"userId\":\"' + uid + '\"}'
                        response = json.dumps({"op":"USER_CREATED","userId":uid})

        # request format : {"op":"CREATE_LANDMARK", "username":<username>, "uid":<uid>, "landmark":<landmark-json-string> }
        # response format : { "op":"LANDMARK_CREATED", "userId":<uid> }
        # error format : { "op": "ERROR" }
        # checks users_database for already existing landmark, then creates landmark.
        elif op == "CREATE_LANDMARK":
                #TODO: Create a landmark object for a user.
                user_landmarks = landmark_database.get(user)
                if user_landmarks is not None:
                        landmark_database[user].append(data_as_json["landmark"])
                        response = json.dumps({"op":"LANDMARK_CREATED", "userId":uid})
                pass

        # request format : {"op":"FETCH_LANDMARKS", "username":<username>, "uid":<uid> }
        # response format : { "op":"FETCHED_LANDMARK_COLLECTION", 
        # "userId":<uid>, 
        # "landmarks":<comma separated landmark-json-strings> }
        # error format : { "op": "ERROR" }
        # checks users_database for existing user
        # returns the landmark for a specific user
        elif op == "FETCH_LANDMARKS":
                #TODO: Return landmarks for specific user.
                pass

        complete = True

        if complete:
                c.send(response.encode('utf-8'))
                print("Response Sent: ", response)

        print(end, "\n#########################")
        c.close()
