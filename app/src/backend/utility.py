def createUserID(username, password):
    if username == "TEST_USER":
        return "TEST_UID"
    return str(abs(hash(username)) + abs(hash(password)))

def printUserDB(db):
    for k, v in db.items():
        print(k,v)

def printLandmarkDB(db):
    for lm in db:
        print(lm)