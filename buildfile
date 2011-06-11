require 'buildr/scala'

repositories.remote << "http://www.ibiblio.org/maven2/"

define 'build-file' do
  project.version = '0.1.0'
  package :jar
end
