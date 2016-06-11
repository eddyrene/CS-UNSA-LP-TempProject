#-*- coding: utf-8 -*-
import re
import io
from pymongo import MongoClient
import gridfs

#bd_url="mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101"
bd_url="mongodb://juanjo:1234@ds015713.mlab.com:15713/prueba"#es mi url trabajare con ella
class mongo:
    db=0
    connection=0
    viviendas=[]
    db_img=0
    fs=0
    def __init__(self):
        try:
            self.connection = MongoClient(bd_url)
            self.db = self.connection.prueba.Cuartos
            self.db_img=self.connection.prueba
            self.fs = gridfs.GridFS(self.db_img)
            print ('conexion sin problemas')
        except ValueError:
            print ('error en la conexion')
    def insert_cuarto(self,ID,Distrito,coord,servicios,nombre,precio,genero):
        vivienda ={
                "ID":ID,
                "Distrito":Distrito,
                "Coord":{'coord1':coord[0],'coord2':coord[1]},#dos coordenadas posicion en el mapa
                "Nombre":nombre,
                "Servicios":{'baño':servicios[0] ,'tv':servicios[1] ,'ducha':servicios[2],'wifi':servicios[3]},#baño,tv,ducha,mascota con 0 y 1
                "Precio":precio,
                "Genero":genero# 1 es solo hombres 2 es solo chicas y tres es los dos
                   }
        try:
            self.db.insert_one(vivienda).inserted_id
        except ValueError:
            print ('No se pudo insertear')
            
    def mostrar(self):
        results=self.db.find()
        for record in results:
            print(record['Nombre'])
    def contar_docu(self):#contar documentos de una colllection
        return self.db.count()
    
    def eliminar (self,ID):#probar
        self.db.delete_one({'ID':ID})
        
    def desconectar(self):
        self.connection.close()
        
    def mas_baratos(self,distrito,genero,servicios,precio_min,precio_max):#podemos modificarlo para una solo universidad
        result=self.db.find({'Distrito':distrito,'Genero':genero,'Servicios.baño':servicios[0],'Servicios.tv':servicios[1],'Servicios.ducha':servicios[2],'Servicios.wifi':servicios[3],'$and': [{'Precio':{'$gte':precio_min}},{'Precio':{'$lte':precio_max}}]})
        for record in result:
           print record['ID']
    def insertar_imagen(self,path,nombre):
        
        filename = path #este un ejemplo "C:\Users\Juanjo\Desktop\casa.png"
        datafile = open(filename,"rb");
        thedata = datafile.read()
        self.fs.put(thedata, filename=nombre)
        datafile.close()
    def sacar_imagen(self,nombre,path,nuevo_nombre):#el nombre con el q se guardo en la BD
        path=path+"\ "+nuevo_nombre
        outputdata =self.fs.get_last_version(nombre).read()
        outfilename = path#donde se guardara la imagen ejemplo "C:\Users\Juanjo\Desktop\casa.png"
        output= open(outfilename,"wb")     
        output.write(outputdata)
        output.close()
    #baño,tv,ducha,wifi
if __name__ == "__main__":
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
    mongo1=mongo()
    #mongo1.mas_baratos('Avelino','3',['1','0','1','0'],'100','130')#distrito,genero(1 o 2 o 3),servicios(baño,tv,ducha,wifi),precio min ,precio max)
    #mongo1.desconectar()
    #mongo1.insertar_imagen("C:\Users\Juanjo\Desktop\casa.png",'casa')
    mongo1.sacar_imagen('casa','C:\Users\Juanjo\Desktop','casa_5.png')
    '''mongo1.insert_cuarto('1','Cayma',['123','12'],['1','0','1','1'],'Seoa','300','3')
    mongo1.insert_cuarto('2','Cayma',['123','12'],['1','1','1','1'],'Seoa','70','1')
    mongo1.insert_cuarto('3','Avelino',['123','12'],['1','0','1','0'],'Seoa','120','3')
    mongo1.insert_cuarto('4','Avelino',['123','12'],['1','1','1','1'],'Seoa','100','3')
    mongo1.insert_cuarto('5','Avelino',['123','12'],['1','0','1','0'],'Seoa','180','3')'''
    #mongo1.mas_baratos('70','100')
    #mongo1.mostrar()
    #mongo1.desconectar()
    #mongo1.mostrar
    #conexion(db)
    #insert_vivienda('12','43','acari','pueblo_lindo','100')
    #mostrar(db)
    #connection.close()
