from socket import *
from utility import *
import json

while True:
    s = socket()
    s.connect(("localhost",8080))

    test_request = input("Enter the request you want to test: ")
    request = ""
    
    #LOGIN    
    if test_request == "LOGIN":
        request = json.dumps({"op":"LOGIN","username":"TEST_USER","password":"TEST_PASSWORD"})
    #CREATE_USER
    elif test_request == "CREATE_USER":
        request = json.dumps({"op":"CREATE_USER","username":"TEST_USER","password":"TEST_PASSWORD"})
    #CREATE_LANDMARK
    elif test_request == "CREATE_LANDMARK":
        test_landmark = "37.3599 -122.0098 : Sunnyvale is a nice place."
        request = json.dumps({"op":"CREATE_LANDMARK","username":"TEST_USER","uid":"TEST_UID","landmark-object":test_landmark})
    #FETCH_LANDMARKS
    elif test_request == "FETCH_LANDMARKS":
        request = json.dumps({"op":"FETCH_LANDMARKS", "username":"TEST_USER","uid":"TEST_UID"})
        
    s.send(request.encode('utf-8'))
    response = s.recv(1024)
    print("server response: ", response.decode())

