class CreateBooks < ActiveRecord::Migration[5.2]
  def change
    create_table :books do |t|
      t.string :title
      t.string :author
      t.string :desc
      t.date :pubdate
      t.integer :isbn
      t.string :cover_path
      t.integer :length

      t.timestamps
    end
  end
end
