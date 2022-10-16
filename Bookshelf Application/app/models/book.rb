class Book < ApplicationRecord
    validates :title, presence: true
    validates :author, presence: true
    validates :desc, presence: true, length: { minimum: 10 }
    validates :pubdate, presence: true
    validates :isbn, presence: true
    validates :cover_path, presence: true
    validates :length, presence: true
end
