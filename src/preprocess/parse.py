import pandas as pd

infile='IMDB_Dataset.csv'
outfile='IMDB_Dataset_fixed.csv'

data=pd.read_csv(infile)

print ( data.columns )
print ( data.shape )

review=[line.replace(',',';') for line in data['review']]

data['review']=review

data.to_csv(outfile,index=False,header=True)
