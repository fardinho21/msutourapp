def createUserID(username, password):
    if username == "TEST_USER":
        return "TEST_UID"
    return str(abs(hash(username)) + abs(hash(password)))

def parseLandmarkInfo(landmark_info):
    #TODO: parse landmark string with name added #DONE
    info = landmark_info.split(":")
    info = info[0].strip().split(" ") + [info[1].strip()] + [info[2].strip()]
    return info

def createLandmarkID(landmark_description, uid):
    i = 0
    t = 0
    for c in landmark_description:
        i+=abs(hash(c))
    if uid == "TEST_UID":
        for c in uid:
            t+=abs(hash(c))
            return i^t
    return i^int(uid)

def createLandmark(landmark_info, uid):
    lm_info = parseLandmarkInfo(landmark_info)
    lm_id = createLandmarkID(lm_info[2], uid)
    return (lm_id, lm_info)

def printUserDB(db):
    for k, v in db.items():
        print(k,v)

def printLandmarkDB(db):
    for lm in db:
        print(lm)
