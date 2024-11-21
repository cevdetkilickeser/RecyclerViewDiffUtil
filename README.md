# RecyclerView DiffUtil Kullanımı
Bu repo, RecyclerView'de kullanılan `DiffUtil` sınıfının nasıl çalıştığını ve ne işe yaradığını göstermek için hazırlanmıştır. 

## Amaç
`DiffUtil`, RecyclerView listelerindeki verilerin güncellenmesini optimize etmek için kullanılan bir yardımcı sınıftır. Bu repo, aşağıdaki sorulara cevap bulmanıza yardımcı olacaktır:
- `DiffUtil` nedir ve neden kullanılır?
  DiffUtil iki liste arasındaki farkları hesaplamak için tasarlanmış bir yardımcı sınıftır ve temel amacı, verimli bir şekilde liste güncellemelerini işlemek ve değişiklikleri tespit
  etmektir.
- `DiffUtil` ile RecyclerView performansı nasıl artırılır?
  DiffUtil RecyclerView listesinde bir değişiklik olduğunda, sadece değişiklik olan item'ları yeniden çizerek bellek kullanımını azaltır ve performans artışı sağlar. Özellikle büyük
  listelerde fark yaratır.
- `DiffUtil.Callback` nasıl uygulanır?
  DiffUtil.Callback miras alınarak özellikleri sınıfımıza aktarılır. Bu sınıf aracılığıyla güncel liste ve eski liste arasındaki karşılaştırma yapılır.

## Bu projede
- Basit bir kullanıcı listesi oluşturulacak.
- Listeye ekleme, silme ve güncelleme işlemleri yapılacak.
- `DiffUtil` kullanılarak RecyclerView güncelleme performansı artırılacak.
- `notifyDataSetChanged` metodu kullanılarak DiffUtil ile arasındaki fark gösterilecek.

### UserDiffCallback

```kotlin
class UserDiffCallback(
    private val oldList: List<User>,
    private val newList: List<User>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
```
Oluşturacağımmız DiffCallback sınıfı constructor'da karşılaştırma yapmak üzere eski liste ve yeni liste alır. DiffUtil.Callback sınıfını da miras olarak alır. Bu sınıfın miras alınmasıyla,
karşılaştırma yapmak için kullanılan metodlar implement edilmek zorundadır. Buradaki fonksiyonlar DiffUtil'ın nasıl farkları bulması gerektiğini tanımlar, hangi öğelerin eşleşip 
eşleşmediğini ve içeriklerinin aynı olup olmadığını kontrol eder.

### UserAdapterDiffUtil

```kotlin
class UserAdapterDiffUtil : RecyclerView.Adapter<UserViewHolder>() {

    private var userList: List<User> = emptyList()

    fun updateList(newList: List<User>) {
        val diffCallback = UserDiffCallback(userList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        userList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(layoutInflater)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
        Log.d("Log of RecyclerView", "Yeniden Çizilen Kullanıcı -> ${user.name}")
    }

    override fun getItemCount(): Int = userList.size
}
```
Eski liste ile karşılaştırılmak üzere, `updateList` fonksiyonuna güncellenmiş liste parametre olarak geçirilir. Bu fonksiyon içinde oluşturulan `DiffCallback` nesnesi de, `DiffUtil` sınıfının 
`calculateDiff` methoduna parametre olarak geçilerek karşılaştırma yapılır. Yeni liste, eski listeye atanarak güncelleme yapılır. `dispatchUpdatesTo` metodu, `diffResult`'taki farkların nasıl 
uygulanacağını belirtir. Bu metot, `DiffUtil.DiffResult` nesnesini kullanarak, farkları uygun bir şekilde RecyclerView.Adapter'a bildirir. 
(burada this ifadesi, RecyclerView.Adapter sınıfını ifade eder). Yani, farkları hesapladıktan sonra, bu değişikliklerin (örneğin, ekleme, silme, güncelleme) RecyclerView'a yansıtılması 
sağlanır. Bu işlem, RecyclerView'ın veriyi güncellemesini ve animasyonları düzgün bir şekilde uygulamasını sağlar.

`onBindViewHolder` metoduna eklediğimiz `Log` ile item'lar he çizildiğinde Logcat'ten izleyebileceğiz. `DiffUtil` kullandığımızda sadece yeni eklenen ve güncellenen item'ların yeniden çizildiğini,
`notifyDataSetChanged` metodu ile kullanıldığında ise tüm item'ların yenien çizildiğini göreceğiz. 

### Logcat
  **notifyDataSetChanged**
  
<pre>
  // Sayfa açıldığında adapter'daki boş liste yerine, activity içerisinde oluşturulan userList eklendi.
  
2024-11-21 12:03:59.879 18906-18906 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> İsim: Ahmet, Yaş: 25
2024-11-21 12:03:59.881 18906-18906 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> İsim: Ayşe, Yaş: 30
2024-11-21 12:03:59.883 18906-18906 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> İsim: Mehmet, Yaş: 20
2024-11-21 12:10:55.815 18906-18906 Log of RecyclerView       D  Reset clicked.
  
  // Reset butonu ilk oluşturulan userList'i, adapter'ın updateList fonksiyonuna parametre olarak verir. 
  
2024-11-21 12:10:55.828 18906-18906 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> İsim: Ahmet, Yaş: 25
2024-11-21 12:10:55.828 18906-18906 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> İsim: Ayşe, Yaş: 30
2024-11-21 12:10:55.829 18906-18906 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> İsim: Mehmet, Yaş: 20

  //Listede hiç değişiklik yok ama tüm item'lar yeniden çizildi.
  
2024-11-21 12:29:25.526 18906-18906 Log of RecyclerView       D  Update clicked.
  
  // Update butonu userList'e göre güncellenmiş olan newList'i adapter'ın updateList fonksiyonuna parametre olarak verir.
  
2024-11-21 12:29:25.542 18906-18906 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> İsim: Ahmet, Yaş: 26
2024-11-21 12:29:25.543 18906-18906 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> İsim: Ayşe, Yaş: 30
2024-11-21 12:29:25.549 18906-18906 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> İsim: Fatma, Yaş: 22

  //Ayşe kullanıcısında değişiklik yok. Buna rağmen item çizildi.
</pre>

**notifyDataSetChanged**
  
<pre>
  // Sayfa açıldığında adapter'daki boş liste yerine, activity içerisinde oluşturulan userList eklendi.
  
2024-11-21 12:35:04.825 20130-20130 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> Ahmet
2024-11-21 12:35:04.830 20130-20130 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> Ayşe
2024-11-21 12:35:04.838 20130-20130 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> Mehmet
2024-11-21 12:35:14.453 20130-20130 Log of RecyclerView       D  Reset clicked.
2024-11-21 12:35:16.196 20130-20130 Log of RecyclerView       D  Reset clicked.
2024-11-21 12:35:18.345 20130-20130 Log of RecyclerView       D  Reset clicked.

  // Reset butonuna tıklandığında adapter'daki userList'in aynısını tekrar adapter'a verdiği için, DiffUtil fark bulamıyor ve itemlar yeniden çizilmiyor.
  
2024-11-21 12:35:22.134 20130-20130 Log of RecyclerView       D  Update clicked.
2024-11-21 12:35:22.142 20130-20130 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> Ahmet
2024-11-21 12:35:22.144 20130-20130 Log of RecyclerView       D  Yeniden Çizilen Kullanıcı -> Fatma

  // Update butonunun gönderdiği newList içinde Ayşe kullanıcısında değişikilk olmadığı için bu veriyi barındıran item  yeniden çizilmiyor.
</pre>

### notify metodları
`notifyDataSetChange` metodunun tüm listeyi çizdiğini biliyoruz ve bu performans olarak DiffUtil'e göre dezavantajlı. Bunun yanında `notifyItemChanged`, `notifyItemInserted` ve 
`notifyItemRemoved` gibi yöntemler, RecyclerView'ı manuel olarak güncellemek için kullanılır. Ancak, bu yöntemlerin DiffUtil'e göre bazı dezavantajları vardır:

1. `notifyItemChanged()`
   Bu yöntem, belirli bir öğeyi güncellemek için kullanılır. Ancak sadece değişen öğeyi günceller. Eğer öğede gerçek bir değişiklik yoksa, gereksiz yere tüm öğe yeniden çizilir.

   **Dezavantajlar:**
   - **Yanlış güncelleme:** Bu metodu kullanarak öğenin gerçekten değişip değişmediğini kontrol etmek zor olabilir. Yani, sadece öğenin değiştiği sanılır, ancak aslında öğe değişmemiştir.
   Bu durumda gereksiz yere öğe yeniden çizilmiş olur.
   - **Performans kaybı:** Eğer çok sayıda öğe güncelleniyorsa ve gereksiz yere `notifyItemChanged()` çağrılıyorsa, bu performans kaybına neden olabilir.

2. `notifyItemInserted()`
   Bu yöntem, yeni bir öğe eklemek için kullanılır. Ancak, manuel olarak ekleme işlemi yaparken, doğru ekleme pozisyonunun belirlenmesi gerekmektedir.

   **Dezavantajlar:**
   - **Pozisyon hataları:** Listeyi değiştirdiğinizde, yeni öğe ekledikten sonra mevcut öğelerin pozisyonları değişebilir. Bu, yanlış pozisyonda öğe eklemeye neden olabilir.
   - **Manuel kontrol:** Yeni öğeleri eklerken, eski listenin doğru pozisyonunda olduğundan emin olmak zor olabilir. Bu da hatalı eklemelere neden olabilir.
   - **Daha fazla kod:** Öğenin nereden eklenmesi gerektiğini manuel olarak belirlemek için daha fazla kod yazmanız gerekebilir.

3. `notifyItemRemoved()`
   Bu yöntem, belirli bir öğeyi silmek için kullanılır. Ancak öğe silindikten sonra, sonraki öğelerin pozisyonları güncellenmelidir.

   **Dezavantajlar:**
   - **Pozisyon güncellemeleri:** Öğeleri manuel olarak sildiğinizde, silinen öğenin ardından gelen öğelerin pozisyonları değişir. Bu, her öğenin doğru pozisyonda kaldığından emin olmak
   için ekstra kod yazmanızı gerektirir.
   - **Yüksek karmaşıklık:** Öğenin doğru şekilde silinip silinmediğini, ve sonrasında pozisyonların doğru şekilde güncellenip güncellenmediğini izlemek karmaşık olabilir.

  Özetlemek gerekirse, liste güncellemelerimzde DiffUtil bize gereksiz yeniden çizilmeleri önleyerek performans ve değişiklikleri kendisi algılayarak kolaylık sağlar. 
