'''
    Simple socket server using threads
'''
 
import socket
import sys
 
HOST = 'localhost'   # Symbolic name, meaning all available interfaces
PORT = 8888 # Arbitrary non-privileged port
 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print 'Socket created'
 
#Bind socket to local host and port
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
     
print 'Socket bind complete'
 
#Start listening on socket
s.listen(10)
print 'Socket now listening'
 
#now keep talking with the client
while True:
        current_connection, address = s.accept()
        print("Hey, Connected")

        while True:
            data = current_connection.recv(2048)
            print data

            if data == 'quit\r\n':
                current_connection.shutdown(1)
                current_connection.close()
                break

            elif data == 'stop\r\n':
                current_connection.shutdown(1)
                current_connection.close()

            elif data:
                current_connection.send("Response" + data)
                print data
     
s.close()