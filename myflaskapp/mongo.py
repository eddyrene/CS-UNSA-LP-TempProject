import re
import io
from pymongo import MongoClient
import gridfs

bd_url="mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101"

class mongo:
    db=0
    connection=0
    viviendas=[]
    def __init__(self):
        try:
            self.connection = MongoClient(bd_url)
            self.db = self.connection.prueba.Cuartos
            print ('conexion sin problemas')
        except ValueError:
            print ('error en la conexion')

    def insert_cuarto(self,ID,coord,servicios,nombre,precio,genero):
        vivienda ={
                "ID":ID,
                "Coord":[coord[0],coord[1]],
                "Nombre":nombre,
                "Servicios":[servicios[0] , servicios[1] ,servicios[2] ],
                "Precio":precio,
                "Genero":genero
                   }
        try:
            self.db.insert_one(vivienda).inserted_id
        except ValueError:
            print ('No se pudo insertar')
            
    def mostrar(self):
        results=self.db.find()
        for record in results:
            print(record['Nombre'])
    def contar_docu(self):#contar documentos de una colllection
        return self.db.count()
    
    def eliminar (self,ID):#probar
        self.db.delete_one({'ID':ID})
        
    '''def mas_baratos(self,precio_min,precio_max):#podemos modificarlo para una solo universidad
        result=self.db.find({'Precio':{'$gte':precio_min}},'$and:'{'Precio':{'$lte':precio_min}}})
        for record in result:
            print(record['Nombre'])'''
    
    
    def desconectar(self):
        self.connection.close()



#if __name__ == "__main__":
    #connection = MongoClient("mongodb://juanjo:1234@ds015713.mlab.com:15713/prueba")
    #db = connection.prueba.docs101
    '''filename = "C:\Users\Juanjo\Desktop\casa.png"
    datafile = open(filename,"r");
    thedata = datafile.read()
    connection = MongoClient("mongodb://juanjo:1234@ds015713.mlab.com:15713/prueba")
    db =connection.prueba
    fs = gridfs.GridFS(db)
    stored = fs.put(thedata, filename="testimage") 
    outputdata =fs.get(stored).read()
    outfilename = "C:\Users\Juanjo\Desktop\casa_2.png"
    output= open(outfilename,"w")     
    output.write(outputdata)
    output.close()#imagen'''
    #mongo1=mongo()
    #mongo1.insert_cuarto('1',['123','12'],['1','0','1','1','0'],'Seoa','70','3')
    #mongo1.insert_cuarto('1',['123','12'],['1','0','1','1','0'],'Seoa','50','3')
    #print mongo1.mas_baratos('70',100)
    #mongo1.mostrar()
    #mongo1.desconectar()
    #mongo1.mostrar
    #conexion(db)
    #insert_vivienda('12','43','acari','pueblo_lindo','100')
    #mostrar(db)
    #connection.close()
